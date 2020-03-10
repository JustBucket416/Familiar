package justbucket.familiar.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import justbucket.familiar.domain.feature.master.DeleteModel
import justbucket.familiar.domain.feature.master.LoadAllModels
import justbucket.familiar.domain.feature.master.SaveModel
import justbucket.familiar.domain.feature.search.SearchByQuery
import justbucket.familiar.extension.model.MasterModel
import justbucket.familiar.resource.Resource

/**
 * @author JustBucket on 2019-07-15
 */
class MasterViewModel(
    private val loadAllModels: LoadAllModels,
    private val searchByQuery: SearchByQuery,
    private val deleteModel: DeleteModel,
    private val saveModel: SaveModel
) : BaseViewModel<Set<MasterModel>>() {

    private val deleteModelEventData = MutableLiveData<Resource<Long>>()

    fun loadModels() {
        liveData.postValue(Resource.loading())
        loadAllModels.execute(viewModelScope,
            {
                if (it.second.isNotEmpty()) {
                    liveData.postValue(Resource.success(it.second))
                } else {
                    liveData.postValue(Resource.error("No data found"))
                }
            }
        )
    }

    fun deleteModel(id: Long) {
        deleteModelEventData.postValue(Resource.loading())
        deleteModel.execute(
            viewModelScope,
            onResult = {
                it.either(
                    { deleteModelEventData.postValue(Resource.error(it.errorMessage)) },
                    { deleteModelEventData.postValue(Resource.success(it)) })
            },
            params = DeleteModel.Params.createParams(id)
        )
    }

    fun saveModel(model: MasterModel) {
        saveModel.execute(
            viewModelScope,
            onResult = {},
            params = SaveModel.Params.createParams(model)
        )
    }

    fun loadContent(query: String) {
        searchByQuery.execute(
            viewModelScope,
            {
                if (it.isNotEmpty()) {
                    liveData.postValue(Resource.success(it))
                } else {
                    liveData.postValue(Resource.error("No data found"))
                }
            },
            SearchByQuery.Params.createParams(query)
        )
    }
}