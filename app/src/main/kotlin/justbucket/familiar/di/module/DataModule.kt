package justbucket.familiar.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import justbucket.familiar.data.DetailRepositoryImpl
import justbucket.familiar.data.MasterRepositoryImpl
import justbucket.familiar.data.database.ContentDatabase
import justbucket.familiar.domain.extension.ExtensionManager
import justbucket.familiar.domain.repository.DetailRepository
import justbucket.familiar.domain.repository.MasterRepository

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
        contentDatabase: ContentDatabase,
        detailRepository: DetailRepository
    ): MasterRepository {
        return MasterRepositoryImpl(extensionManager, contentDatabase, detailRepository)
    }

    @Provides
    fun provideDetailRepository(
        extensionManager: ExtensionManager,
        contentDatabase: ContentDatabase
    ): DetailRepository {
        return DetailRepositoryImpl(extensionManager, contentDatabase)
    }
}
