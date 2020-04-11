package justbucket.familiar.di.module

import dagger.Module
import dagger.Provides
import justbucket.familiar.domain.extension.ExtensionManager
import justbucket.familiar.domain.feature.detail.LoadModelDetails
import justbucket.familiar.domain.repository.DetailRepository
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

}
