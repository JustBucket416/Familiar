package justbucket.familiar.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
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

/**
 * @author JustBucket on 2019-07-31
 */
@Module
abstract class ViewModelModule {

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun provideDetailViewModel(loadModelDetails: LoadModelDetails) = DetailViewModel(loadModelDetails)

        @Provides
        @JvmStatic
        fun provideMasterViewModel(loadAllModels: LoadAllModels, deleteModel: DeleteModel) =
            MasterViewModel(loadAllModels, deleteModel)
    }

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    abstract fun bindDetailViewModel(detailViewModel: DetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MasterViewModel::class)
    abstract fun bindActionVIewModel(masterViewModel: MasterViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
