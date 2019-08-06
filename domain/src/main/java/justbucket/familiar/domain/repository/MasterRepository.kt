package justbucket.familiar.domain.repository

import justbucket.familiar.content.extension.model.MasterModel
import justbucket.familiar.domain.exception.Failure
import justbucket.familiar.domain.functional.Either

/**
 * @author JustBucket on 2019-07-12
 */
interface MasterRepository {

    suspend fun loadAllModels(): Either<Failure.DBFailure, Set<MasterModel>>

    suspend fun deleteModel(modelId: Long): Either<Failure.DBFailure, Long>
}