package justbucket.familiar.domain.repository

import justbucket.familiar.domain.exception.Failure
import justbucket.familiar.domain.exception.Failure.DBFailure
import justbucket.familiar.domain.functional.Either
import justbucket.familiar.extension.model.MasterModel

/**
 * @author JustBucket on 2019-07-12
 */
interface MasterRepository {

    suspend fun loadAllModels(): Pair<Failure?, Set<MasterModel>>

    suspend fun deleteModel(modelId: Long): Either<DBFailure, Long>

    suspend fun saveModel(masterModel: MasterModel): Either<DBFailure, Long>
}