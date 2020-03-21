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
        liveData.postValue(Resource.Loading())
        loadModelDetails.execute(
            viewModelScope,
            onResult = { result ->
                result.either({ liveData.postValue(Resource.Error(it.errorMessage)) },
                    { liveData.postValue(Resource.Success(it)) })
            },
            params = LoadModelDetails.Params.createParams(masterModel)
        )
    }
}