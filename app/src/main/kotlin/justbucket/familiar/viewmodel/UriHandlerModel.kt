package justbucket.familiar.viewmodel

import androidx.lifecycle.viewModelScope
import justbucket.familiar.domain.feature.share.SaveShareModel
import justbucket.familiar.extension.model.ShareModel
import justbucket.familiar.resource.Resource

/**
 * @author JustBucket on 2019-08-28
 */
class UriHandlerModel(
    private val saveShareModel: SaveShareModel
) : BaseViewModel<Nothing?>() {

    fun saveModel(model: ShareModel) {
        liveData.postValue(Resource.Loading())
        saveShareModel.execute(
            viewModelScope,
            onResult = {
                it.either(
                    { failure -> liveData.postValue(Resource.Error(failure.errorMessage)) },
                    { liveData.postValue(Resource.Success(null)) }
                )
            },
            params = SaveShareModel.Params.createParams(model),
            cancelLastRequest = false
        )
    }
}