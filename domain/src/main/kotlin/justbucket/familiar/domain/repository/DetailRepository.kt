package justbucket.familiar.domain.repository

import justbucket.familiar.extension.exception.Failure.DBFailure
import justbucket.familiar.extension.functional.Either
import justbucket.familiar.extension.model.DetailModel

/**
 * @author JustBucket on 2019-07-12
 */
interface DetailRepository {

    suspend fun loadModelDetails(modelId: Long): Either<DBFailure, DetailModel>
}