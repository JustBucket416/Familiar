package justbucket.familiar.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import justbucket.familiar.MainActivity

/**
 * @author JustBucket on 2019-07-31
 */
@Module
abstract class UIModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActibity(): MainActivity
}
