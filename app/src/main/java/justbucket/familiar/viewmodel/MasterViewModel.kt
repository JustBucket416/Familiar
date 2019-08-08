package justbucket.familiar.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import justbucket.familiar.content.extension.model.MasterModel
import justbucket.familiar.domain.feature.master.DeleteModel
import justbucket.familiar.domain.feature.master.LoadAllModels
import justbucket.familiar.resource.Resource

/**
 * @author JustBucket on 2019-07-15
 */
class MasterViewModel(
    private val loadAllModels: LoadAllModels,
    private val deleteModel: DeleteModel
) : BaseViewModel<Set<MasterModel>>() {

    val deleteModelEventData = MutableLiveData<Resource<Long>>()

    fun loadModels() {
        liveData.postValue(Resource.loading())
        loadAllModels.execute(viewModelScope,
            {
                it.either({
                    liveData.postValue(Resource.error(null))
                },
                    { list ->
                        liveData.postValue(Resource.success(list))
                    })
            })
    }

    fun deleteModel(id: Long) {
        deleteModelEventData.postValue(Resource.loading())
        deleteModel.execute(
            viewModelScope,
            onResult = {
                it.either({
                    deleteModelEventData.postValue(Resource.error(null))
                },
                    { list ->
                        deleteModelEventData.postValue(Resource.success(list))
                    })
            },
            params = DeleteModel.Params.createParams(id)
        )
    }

}