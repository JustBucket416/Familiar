package justbucket.familiar.domain.extension

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import dalvik.system.PathClassLoader
import justbucket.familiar.content.extension.ExtensionConfigurator
import justbucket.familiar.content.extension.ExtensionLocator
import justbucket.familiar.content.extension.ExtensionModelCreator
import justbucket.familiar.content.extension.constants.EXTENSION_FEATURE_NAME
import justbucket.familiar.content.extension.constants.META_CONTENT_EXTENSION_CONFIGURATOR_NAME
import justbucket.familiar.content.extension.constants.META_CONTENT_EXTENSION_LOADER_NAME
import justbucket.familiar.content.extension.constants.META_CONTENT_EXTENSION_NAME
import justbucket.familiar.content.extension.utils.Logger
import justbucket.familiar.domain.extension.ExtensionManager.Constants.VALID_EXTENSION_NAME
import justbucket.familiar.domain.utils.SingletonHolder
import justbucket.familiar.domain.utils.getOrDie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.util.regex.Pattern

/**
 * @author JustBucket on 2019-07-22
 */
class ExtensionManager private constructor(private var context: Context?) {

    private val extensionHolders = UnsafeHashMap<String, ExtensionHolder>()

    fun getExtensions(): UnsafeHashMap<String, ExtensionHolder> = runBlocking {
        if (extensionHolders.isEmpty()) {
            loadExtensions()
        }
        extensionHolders
    }

    private suspend fun loadExtensions() {
        withContext(Dispatchers.Default) {
            val pm = context?.packageManager
            val packages = pm?.getInstalledPackages(PackageManager.GET_CONFIGURATIONS)
            packages?.forEach { packageInfo ->
                val features = packageInfo.reqFeatures
                features?.forEach {
                    launch {
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
                                Logger.w("Invalid extension name: $extensionName")
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
        context = null
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
            Logger.e(ExtensionManager::class.java.simpleName, null, e)
        } catch (e: LinkageError) {
            Logger.e(ExtensionManager::class.java.simpleName, null, e)
        }
        return null
    }

    private fun extendClassName(className: String, packageName: String) =
        if (className.startsWith(".")) {
            packageName + className
        } else className

    class UnsafeHashMap<K, V> : HashMap<K, V>() {

        override operator fun get(key: K): V {
            return super.get(key).getOrDie("Could not get value by key $key")
        }
    }

    companion object : SingletonHolder<ExtensionManager, Context>(::ExtensionManager)

    private object Constants {
        val VALID_EXTENSION_NAME: Pattern = Pattern.compile("[a-z][a-z0-9]{3,14}")
    }
}