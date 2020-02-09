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
        liveData.postValue(Resource.loading())
        saveShareModel.execute(
            viewModelScope,
            onResult = {
                it.either(
                    { liveData.postValue(Resource.error(it.errorMessage)) },
                    { liveData.postValue(Resource.success(null)) }
                )
            },
            params = SaveShareModel.Params.createParams(model)
        )
    }
}