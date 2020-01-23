package justbucket.familiar.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import justbucket.familiar.di.viewmodel.ViewModelFactory
import justbucket.familiar.di.viewmodel.ViewModelKey
import justbucket.familiar.domain.feature.detail.LoadModelDetails
import justbucket.familiar.domain.feature.master.DeleteModel
import justbucket.familiar.domain.feature.master.LoadAllModels
import justbucket.familiar.viewmodel.DetailViewModel
import justbucket.familiar.viewmodel.MasterViewModel
import javax.inject.Provider
import javax.inject.Singleton

/**
 * @author JustBucket on 2019-07-31
 */
@Module
class ViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    fun provideDetailViewModel(loadModelDetails: LoadModelDetails): ViewModel = DetailViewModel(loadModelDetails)

    @Provides
    @IntoMap
    @ViewModelKey(MasterViewModel::class)
    fun provideMasterViewModel(loadAllModels: LoadAllModels, deleteModel: DeleteModel): ViewModel =
        MasterViewModel(loadAllModels, deleteModel)

    @Singleton
    @Provides
    @JvmSuppressWildcards
    fun bindViewModelFactory(creators: Map<Class<out ViewModel>, Provider<ViewModel>>): ViewModelProvider.Factory =
        ViewModelFactory(creators)
}
