package justbucket.familiar.domain.repository

import justbucket.familiar.content.extension.exception.Failure
import justbucket.familiar.content.extension.functional.Either
import justbucket.familiar.content.extension.model.MasterModel
import justbucket.familiar.domain.exception.DBFailure

/**
 * @author JustBucket on 2019-07-12
 */
interface MasterRepository {

    suspend fun loadAllModels(): Either<DBFailure, Set<MasterModel>>

    suspend fun deleteModel(modelId: Long): Either<DBFailure, Long>
}