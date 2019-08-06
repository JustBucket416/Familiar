package justbucket.familiar.domain.repository

import justbucket.familiar.content.extension.model.DetailModel
import justbucket.familiar.domain.exception.Failure
import justbucket.familiar.domain.functional.Either

/**
 * @author JustBucket on 2019-07-12
 */
interface DetailRepository {

    suspend fun loadModelDetails(modelId: Long): Either<Failure, DetailModel>
}