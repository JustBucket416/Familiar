package justbucket.familiar.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import justbucket.familiar.domain.feature.detail.LoadModelDetails
import justbucket.familiar.domain.repository.MasterRepository
import justbucket.familiar.extension.model.DetailModel
import justbucket.familiar.extension.model.MasterModel
import justbucket.familiar.extension.resource.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * @author JustBucket on 2019-07-15
 */
@ExperimentalCoroutinesApi
class MasterViewModel(
    private val masterRepository: MasterRepository,
    private val loadModelDetails: LoadModelDetails
) : BaseViewModel<List<MasterModel>>() {

    fun loadModels() {
        liveData.postValue(Resource.Loading())
        masterRepository.loadAllModels()
            .onEach { liveData.postValue(Resource.Success(it)) }
            .catch { liveData.postValue(Resource.Error("No data found")) }
            .launchIn(viewModelScope)
    }

    fun deleteModel(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            masterRepository.deleteModel(id)
        }
    }

    fun saveModel(model: MasterModel) {
        viewModelScope.launch(Dispatchers.IO) {
            masterRepository.saveModel(model)
        }
    }

    fun loadContent(query: String) {
        if (query.isNotEmpty()) {
            liveData.postValue(Resource.Loading())
            masterRepository.searchByQuery(viewModelScope, query)
        }
    }

    fun loadDetails(masterModel: MasterModel): LiveData<Resource<DetailModel>> {
        val liveData = MutableLiveData<Resource<DetailModel>>()
        liveData.postValue(Resource.Loading())
        loadModelDetails.execute(
            viewModelScope,
            onResult = { result ->
                result.either({ liveData.postValue(Resource.Error(it.errorMessage)) },
                    {
                        liveData.postValue(Resource.Success(it))
                    })
            },
            params = LoadModelDetails.Params.createParams(masterModel),
            cancelLastRequest = false
        )
        return liveData
    }
}