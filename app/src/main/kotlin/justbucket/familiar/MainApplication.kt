package justbucket.familiar

import android.app.Application
import justbucket.familiar.di.AppComponent
import justbucket.familiar.di.DaggerAppComponent
import justbucket.familiar.domain.extension.ExtensionManager
import justbucket.familiar.domain.utils.IS_LOGGING_ENABLED

/**
 * @author JustBucket on 2019-07-31
 */
class MainApplication : Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()

        IS_LOGGING_ENABLED = BuildConfig.DEBUG

        ExtensionManager.loadExtensions(this)

        component = DaggerAppComponent
            .builder()
            .application(this)
            .build()
    }
}