package justbucket.familiar

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import justbucket.familiar.di.DaggerAppComponent
import javax.inject.Inject

/**
 * @author JustBucket on 2019-07-31
 */
class MainApplication : Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var androidActivityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var androidFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun activityInjector() = androidActivityInjector

    override fun supportFragmentInjector() = androidFragmentInjector

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)
    }
}