package justbucket.familiar.viewmodel

import androidx.lifecycle.viewModelScope
import justbucket.familiar.domain.feature.detail.LoadModelDetails
import justbucket.familiar.extension.model.DetailModel
import justbucket.familiar.extension.model.MasterModel
import justbucket.familiar.resource.Resource

class DetailViewModel(
    private val loadModelDetails: LoadModelDetails
) : BaseViewModel<DetailModel>() {

    fun loadDetails(masterModel: MasterModel) {
        liveData.postValue(Resource.loading())
        loadModelDetails.execute(
            viewModelScope,
            onResult = { result ->
                result.either({ liveData.postValue(Resource.error(it.errorMessage)) },
                    { liveData.postValue(Resource.success(it)) })
            },
            params = LoadModelDetails.Params.createParams(masterModel)
        )
    }
}