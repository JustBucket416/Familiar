package justbucket.familiar.domain.repository

import justbucket.familiar.content.extension.exception.Failure
import justbucket.familiar.content.extension.functional.Either
import justbucket.familiar.content.extension.model.DetailModel
import justbucket.familiar.domain.exception.DBFailure

/**
 * @author JustBucket on 2019-07-12
 */
interface DetailRepository {

    suspend fun loadModelDetails(modelId: Long): Either<DBFailure, DetailModel>
}