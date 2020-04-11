package justbucket.familiar.di.module

import android.app.Application
import android.content.Context
import com.bumptech.glide.Glide
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * @author JustBucket on 2019-07-31
 */
@Module
abstract class AppModule {

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun provideGlideManager(context: Context) = Glide.with(context)

        @Provides
        @JvmStatic
        fun provideCoroutineContext(): CoroutineContext = Dispatchers.Main
    }

    @Binds
    abstract fun bindAppContext(application: Application): Context

}
