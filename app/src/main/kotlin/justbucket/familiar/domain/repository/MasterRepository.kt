package justbucket.familiar.domain.repository

import justbucket.familiar.extension.model.MasterModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * @author JustBucket on 2019-07-12
 */
interface MasterRepository {

    fun loadAllModels(): Flow<List<MasterModel>>

    fun searchByQuery(scope: CoroutineScope, query: String)

    suspend fun deleteModel(modelId: Long)

    suspend fun saveModel(masterModel: MasterModel)
}