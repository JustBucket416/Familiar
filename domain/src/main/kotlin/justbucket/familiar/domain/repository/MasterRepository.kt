package justbucket.familiar.domain.repository

import justbucket.familiar.extension.exception.Failure.DBFailure
import justbucket.familiar.extension.functional.Either
import justbucket.familiar.extension.model.MasterModel

/**
 * @author JustBucket on 2019-07-12
 */
interface MasterRepository {

    suspend fun loadAllModels(): Either<DBFailure, Set<MasterModel>>

    suspend fun deleteModel(modelId: Long): Either<DBFailure, Long>
}