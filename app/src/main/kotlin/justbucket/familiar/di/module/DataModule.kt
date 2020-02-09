package justbucket.familiar.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import justbucket.familiar.data.DetailRepositoryImpl
import justbucket.familiar.data.MasterRepositoryImpl
import justbucket.familiar.data.SearchRepositoryImpl
import justbucket.familiar.data.database.ContentDatabase
import justbucket.familiar.domain.extension.ExtensionManager
import justbucket.familiar.domain.repository.DetailRepository
import justbucket.familiar.domain.repository.MasterRepository
import justbucket.familiar.domain.repository.SearchRepository

/**
 * @author JustBucket on 2019-07-31
 */
@Module
class DataModule {

    @Provides
    fun provideDatabase(context: Context) = ContentDatabase.getInstance(context)

    @Provides
    fun provideMasterRepository(
        extensionManager: ExtensionManager,
        contentDatabase: ContentDatabase
    ): MasterRepository {
        return MasterRepositoryImpl(extensionManager, contentDatabase)
    }

    @Provides
    fun provideSearchRepository(
        extensionManager: ExtensionManager
    ): SearchRepository {
        return SearchRepositoryImpl(extensionManager.getExtensions().values.toList())
    }

    @Provides
    fun provideDetailRepository(
        extensionManager: ExtensionManager,
        contentDatabase: ContentDatabase
    ): DetailRepository {
        return DetailRepositoryImpl(extensionManager, contentDatabase)
    }
}
