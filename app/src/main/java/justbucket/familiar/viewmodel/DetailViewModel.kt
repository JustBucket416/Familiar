package justbucket.familiar.viewmodel

import androidx.lifecycle.viewModelScope
import justbucket.familiar.content.extension.model.DetailModel
import justbucket.familiar.domain.feature.detail.LoadModelDetails
import justbucket.familiar.resource.Resource

class DetailViewModel(
    private val loadModelDetails: LoadModelDetails
) : BaseViewModel<DetailModel>() {

    fun loadDetails(id: Long) {
        liveData.postValue(Resource.loading())
        loadModelDetails.execute(
            viewModelScope,
            onResult = { result ->
                result.either({ liveData.postValue(Resource.error("")) },
                    { liveData.postValue(Resource.success(it)) })
            },
            params = LoadModelDetails.Params.createParams(id)
        )
    }
}