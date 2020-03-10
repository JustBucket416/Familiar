package justbucket.familiar.di.module

import dagger.Module
import dagger.Provides
import justbucket.familiar.domain.extension.ExtensionManager
import justbucket.familiar.domain.feature.detail.LoadModelDetails
import justbucket.familiar.domain.feature.master.DeleteModel
import justbucket.familiar.domain.feature.master.LoadAllModels
import justbucket.familiar.domain.feature.master.SaveModel
import justbucket.familiar.domain.feature.search.SearchByQuery
import justbucket.familiar.domain.repository.DetailRepository
import justbucket.familiar.domain.repository.MasterRepository
import justbucket.familiar.domain.repository.SearchRepository
import kotlin.coroutines.CoroutineContext

/**
 * @author JustBucket on 2019-07-31
 */
@Module
class DomainModule {

    @Provides
    fun provideExtensionManager() = ExtensionManager

    @Provides
    fun provideLoadModelDetailsFeature(context: CoroutineContext, repository: DetailRepository) =
        LoadModelDetails(context, repository)

    @Provides
    fun provideDeleteModelFeature(context: CoroutineContext, repository: MasterRepository) =
        DeleteModel(context, repository)

    @Provides
    fun provideSaveModelFeature(context: CoroutineContext, repository: MasterRepository) =
        SaveModel(context, repository)

    @Provides
    fun provideLoadAllModelsFeature(context: CoroutineContext, repository: MasterRepository) =
        LoadAllModels(context, repository)

    @Provides
    fun provideSearchByQueryFeature(context: CoroutineContext, repository: SearchRepository) =
        SearchByQuery(context, repository)
}
