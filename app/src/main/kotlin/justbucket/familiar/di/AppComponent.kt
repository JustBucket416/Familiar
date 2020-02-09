package justbucket.familiar.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import dagger.BindsInstance
import dagger.Component
import justbucket.familiar.di.module.AppModule
import justbucket.familiar.di.module.DataModule
import justbucket.familiar.di.module.DomainModule
import justbucket.familiar.di.module.ViewModelModule
import javax.inject.Singleton

/**
 * @author JustBucket on 2019-07-31
 */
@Singleton
@Component(
    modules = [AppModule::class,
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

    fun getViewModelFactory(): ViewModelProvider.Factory
}