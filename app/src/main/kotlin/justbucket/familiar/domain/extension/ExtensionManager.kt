package justbucket.familiar.domain.extension

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import dalvik.system.PathClassLoader
import justbucket.familiar.domain.utils.logE
import justbucket.familiar.domain.utils.logW
import justbucket.familiar.extension.ExtensionConfigurator
import justbucket.familiar.extension.ExtensionLocator
import justbucket.familiar.extension.ExtensionModelCreator
import justbucket.familiar.extension.constants.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File


/**
 * @author JustBucket on 2019-07-22
 */
object ExtensionManager {

    private val extensionHolders = NonNullMap<String, ExtensionHolder>()
    private val mutex = Mutex()

    fun getExtensions(): NonNullMap<String, ExtensionHolder> = extensionHolders

    fun loadExtensions(context: Context) = runBlocking {
        val pm = context.packageManager
        val packages = pm?.getInstalledPackages(PackageManager.GET_CONFIGURATIONS)
        packages?.forEach { packageInfo ->
            launch {
                val features = packageInfo.reqFeatures
                features?.forEach {
                    if (it.name == EXTENSION_FEATURE_NAME) {
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
                            data.getString(META_CONTENT_EXTENSION_MODEL_CREATOR_NAME, "")
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
                }
            }
        }
    }

    private suspend fun loadExtension(
        applicationInfo: ApplicationInfo,
        configuratorClassName: String,
        locatorClassName: String,
        creatorClassName: String,
        context: Context
    ): ExtensionHolder? {
        mutex.withLock {
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
                ).newInstance() as ExtensionConfigurator).also { it.context = context }
                val creator = (Class.forName(
                    creatorClassName,
                    false, classLoader
                ).newInstance() as ExtensionModelCreator).also { it.context = context }
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
    }

    private fun extendClassName(className: String, packageName: String) =
        if (className.startsWith(".")) {
            packageName + className
        } else className

    class NonNullMap<K, V : Any> : HashMap<K, V>() {

        override operator fun get(key: K): V = requireNotNull(super.get(key))
    }
}