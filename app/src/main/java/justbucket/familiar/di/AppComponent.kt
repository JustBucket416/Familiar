package justbucket.familiar.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import justbucket.familiar.MainApplication
import justbucket.familiar.di.module.*
import javax.inject.Singleton

/**
 * @author JustBucket on 2019-07-31
 */
@Singleton
@Component(
    modules = [AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        AppModule::class,
        UIModule::class,
        DomainModule::class,
        ViewModelModule::class,
        DataModule::class]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: MainApplication)
}