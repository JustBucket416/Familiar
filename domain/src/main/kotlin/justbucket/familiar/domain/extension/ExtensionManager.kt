package justbucket.familiar.domain.extension

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import dalvik.system.PathClassLoader
import justbucket.familiar.domain.extension.ExtensionManager.Constants.VALID_EXTENSION_NAME
import justbucket.familiar.domain.utils.logE
import justbucket.familiar.domain.utils.logW
import justbucket.familiar.extension.ExtensionConfigurator
import justbucket.familiar.extension.ExtensionLocator
import justbucket.familiar.extension.ExtensionModelCreator
import justbucket.familiar.extension.constants.EXTENSION_FEATURE_NAME
import justbucket.familiar.extension.constants.META_CONTENT_EXTENSION_CONFIGURATOR_NAME
import justbucket.familiar.extension.constants.META_CONTENT_EXTENSION_LOADER_NAME
import justbucket.familiar.extension.constants.META_CONTENT_EXTENSION_NAME
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.regex.Pattern

/**
 * @author JustBucket on 2019-07-22
 */
object ExtensionManager {

    private val extensionHolders = NonNullMap<String, ExtensionHolder>()

    fun getExtensions(): NonNullMap<String, ExtensionHolder> = extensionHolders

    fun loadExtensions(context: Context) = runBlocking {
        val pm = context.packageManager
        val packages = pm?.getInstalledPackages(PackageManager.GET_CONFIGURATIONS)
        packages?.forEach { packageInfo ->
            launch {
                val features = packageInfo.reqFeatures
                features?.forEach {
                    if (it.name == EXTENSION_FEATURE_NAME) {
                        val applicationInfo: ApplicationInfo
                        try {
                            applicationInfo =
                                pm.getApplicationInfo(packageInfo.packageName, PackageManager.GET_META_DATA)
                        } catch (e: PackageManager.NameNotFoundException) {
                            throw RuntimeException(e)
                        }
                        val data = applicationInfo.metaData
                        val extensionName = data.getString(META_CONTENT_EXTENSION_NAME)
                        if (extensionName == null || !VALID_EXTENSION_NAME.matcher(extensionName).matches()) {
                            logW("Invalid extension name: $extensionName")
                        }
                        val classConfiguration = data.getString(META_CONTENT_EXTENSION_CONFIGURATOR_NAME, "")
                        val classLoader = data.getString(META_CONTENT_EXTENSION_LOADER_NAME, "")
                        val configuratorName = extendClassName(classConfiguration, packageInfo.packageName)
                        val locatorName = extendClassName(classConfiguration, packageInfo.packageName)
                        val loaderName = extendClassName(classLoader, packageInfo.packageName)
                        val extensionItem =
                            loadExtension(applicationInfo, configuratorName, loaderName, locatorName)
                        if (extensionItem != null && extensionName != null) {
                            extensionHolders[extensionName] = extensionItem
                        }
                    }
                }
            }
        }
    }

    private fun loadExtension(
        applicationInfo: ApplicationInfo,
        configuratorClassName: String,
        locatorClassName: String,
        loaderClassName: String
    ): ExtensionHolder? {
        try {
            var nativeLibraryDir: String? = applicationInfo.nativeLibraryDir
            if (nativeLibraryDir != null && !File(nativeLibraryDir).exists()) {
                nativeLibraryDir = null
            }
            val classLoader = PathClassLoader(
                applicationInfo.sourceDir, nativeLibraryDir,
                ExtensionManager::class.java.classLoader
            )
            val configurator = Class.forName(
                configuratorClassName,
                false, classLoader
            ).newInstance() as ExtensionConfigurator
            val loader = Class.forName(
                loaderClassName,
                false, classLoader
            ).newInstance() as ExtensionModelCreator
            val locator = Class.forName(
                locatorClassName,
                false, classLoader
            ).newInstance() as ExtensionLocator
            return ExtensionHolder(configurator, loader, locator)
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

    class NonNullMap<K, V: Any> : HashMap<K, V>() {

        override operator fun get(key: K): V = requireNotNull(super.get(key))
    }

    private object Constants {
        val VALID_EXTENSION_NAME: Pattern = Pattern.compile("[a-z][a-z0-9]{3,14}")
    }
}