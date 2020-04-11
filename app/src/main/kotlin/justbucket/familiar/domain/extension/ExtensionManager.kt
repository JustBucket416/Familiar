package justbucket.familiar.domain.extension

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.appcompat.view.ContextThemeWrapper
import dalvik.system.PathClassLoader
import justbucket.familiar.extension.ExtensionConfigurator
import justbucket.familiar.extension.ExtensionLocator
import justbucket.familiar.extension.ExtensionModelMapper
import justbucket.familiar.extension.constants.*
import justbucket.familiar.utils.NonNullMap
import justbucket.familiar.utils.logE
import justbucket.familiar.utils.logI
import justbucket.familiar.utils.logW
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.lang.reflect.Field
import java.lang.reflect.Modifier

/**
 * @author JustBucket on 2019-07-22
 */
object ExtensionManager {

    private val extensionHolders = NonNullMap<String, ExtensionHolder>()

    fun getExtensions(): NonNullMap<String, ExtensionHolder> = extensionHolders

    fun loadExtensions(context: Context) = runBlocking {
        val pm = context.packageManager
        val packages = pm?.getInstalledPackages(PackageManager.GET_CONFIGURATIONS)
        packages?.filter { it.reqFeatures?.any { it.name == EXTENSION_FEATURE_NAME } == true }
            ?.map { packageInfo ->
                launch(Dispatchers.IO) {
                    val applicationInfo = try {
                        pm.getApplicationInfo(
                            packageInfo.packageName,
                            PackageManager.GET_META_DATA
                        )
                    } catch (e: PackageManager.NameNotFoundException) {
                        throw RuntimeException(e)
                    }
                    val data = applicationInfo.metaData
                    val extensionName = data.getString(META_CONTENT_EXTENSION_NAME)
                    if (extensionName == null) {
                        logW("Invalid extension name: $extensionName")
                    }
                    val classConfigurator =
                        data.getString(META_CONTENT_EXTENSION_CONFIGURATOR_NAME, "")
                    val classLocator =
                        data.getString(META_CONTENT_EXTENSION_LOCATOR_NAME, "")
                    val classLoader =
                        data.getString(META_CONTENT_EXTENSION_MODEL_MAPPER_NAME, "")
                    val configuratorName =
                        extendClassName(classConfigurator, packageInfo.packageName)
                    val locatorName = extendClassName(classLocator, packageInfo.packageName)
                    val creatorName = extendClassName(classLoader, packageInfo.packageName)
                    val extensionItem =
                        loadExtension(
                            applicationInfo,
                            configuratorName,
                            locatorName,
                            creatorName,
                            context
                        )
                    if (extensionItem != null && extensionName != null) {
                        extensionHolders[extensionName] = extensionItem
                    }
                }
            }?.joinAll()
        logI(message = "extensions loaded")
    }

    private fun loadExtension(
        applicationInfo: ApplicationInfo,
        configuratorClassName: String,
        locatorClassName: String,
        creatorClassName: String,
        context: Context
    ): ExtensionHolder? {
        try {
            val nativeLibraryDir: String? =
                applicationInfo.nativeLibraryDir?.takeIf { File(it).exists() }
            val classLoader = PathClassLoader(
                applicationInfo.sourceDir, nativeLibraryDir,
                ExtensionManager::class.java.classLoader
            )
            val configurator = (Class.forName(
                configuratorClassName,
                false, classLoader
            ).newInstance() as ExtensionConfigurator).also {
                it.themedAppContext = prepareThemedExtensionContext(applicationInfo, context)
            }
            val creator = (Class.forName(
                creatorClassName,
                false, classLoader
            ).newInstance() as ExtensionModelMapper)
            val locator = (Class.forName(
                locatorClassName,
                false, classLoader
            ).newInstance() as ExtensionLocator).also { it.context = context }
            return ExtensionHolder(configurator, creator, locator)
        } catch (e: Exception) {
            logE(ExtensionManager::class.java.simpleName, null, e)
        } catch (e: LinkageError) {
            logE(ExtensionManager::class.java.simpleName, null, e)
        }
        return null
    }

    private fun extendClassName(className: String, packageName: String) =
        if (className.startsWith(".")) {
            packageName + className
        } else className

    private fun prepareThemedExtensionContext(
        appInfo: ApplicationInfo,
        baseContext: Context
    ): Context {
        val tempContext = baseContext.createPackageContext(appInfo.packageName, 0)
        val loader = PathClassLoader(
            appInfo.sourceDir,
            appInfo.nativeLibraryDir?.takeIf { File(it).exists() },
            null
        )
        tempContext.classLoader.setPrivateFinalField("parent", loader)
        return ContextThemeWrapper(tempContext, baseContext.theme)
    }

    private inline fun <reified T> T.setPrivateFinalField(fieldName: String, newValue: Any) {
        val field = T::class.java.getDeclaredField(fieldName)
        field.isAccessible = true

        val modifiersField = Field::class.java.getDeclaredField("accessFlags")
        modifiersField.isAccessible = true
        modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())

        field.set(this, newValue)

        modifiersField.setInt(field, field.modifiers and Modifier.FINAL)
        modifiersField.isAccessible = false

        field.isAccessible = false
    }
}