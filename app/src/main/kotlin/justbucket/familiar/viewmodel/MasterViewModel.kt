package justbucket.familiar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import justbucket.familiar.domain.feature.detail.LoadModelDetails
import justbucket.familiar.domain.feature.master.DeleteModel
import justbucket.familiar.domain.feature.master.LoadAllModels
import justbucket.familiar.domain.feature.master.SaveModel
import justbucket.familiar.domain.feature.search.SearchByQuery
import justbucket.familiar.extension.model.DetailModel
import justbucket.familiar.extension.model.MasterModel
import justbucket.familiar.resource.Resource

/**
 * @author JustBucket on 2019-07-15
 */
class MasterViewModel(
    private val loadAllModels: LoadAllModels,
    private val searchByQuery: SearchByQuery,
    private val deleteModel: DeleteModel,
    private val saveModel: SaveModel,
    private val loadModelDetails: LoadModelDetails
) : BaseViewModel<Set<MasterModel>>() {

    private val deleteModelEventData = MutableLiveData<Resource<Long>>()
    private val detailModelData = MutableLiveData<Resource<DetailModel>>()

    fun getDetailModelData(): LiveData<Resource<DetailModel>> = detailModelData

    fun loadModels() {
        liveData.postValue(Resource.Loading())
        loadAllModels.execute(viewModelScope,
            {
                if (it.second.isNotEmpty()) {
                    liveData.postValue(Resource.Success(it.second))
                } else {
                    liveData.postValue(Resource.Error("No data found"))
                }
            }
        )
    }

    fun deleteModel(id: Long) {
        deleteModelEventData.postValue(Resource.Loading())
        deleteModel.execute(
            viewModelScope,
            onResult = {
                it.either(
                    { deleteModelEventData.postValue(Resource.Error(it.errorMessage)) },
                    { deleteModelEventData.postValue(Resource.Success(it)) })
            },
            params = DeleteModel.Params.createParams(id),
            cancelLastRequest = false
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
        if (query.isNotEmpty()) {
            liveData.postValue(Resource.Loading())
            searchByQuery.execute(
                viewModelScope,
                {
                    if (it.isNotEmpty()) {
                        liveData.postValue(Resource.Success(it))
                    } else {
                        liveData.postValue(Resource.Error("No data found"))
                    }
                },
                SearchByQuery.Params.createParams(query)
            )
        }
    }

    fun loadDetails(masterModel: MasterModel) {
        detailModelData.postValue(Resource.Loading())
        loadModelDetails.execute(
            viewModelScope,
            onResult = { result ->
                result.either({ detailModelData.postValue(Resource.Error(it.errorMessage)) },
                    { detailModelData.postValue(Resource.Success(it)) })
            },
            params = LoadModelDetails.Params.createParams(masterModel)
        )
    }
}