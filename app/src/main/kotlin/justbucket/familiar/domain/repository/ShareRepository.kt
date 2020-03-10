package justbucket.familiar.domain.repository

import justbucket.familiar.domain.exception.Failure
import justbucket.familiar.domain.functional.Either
import justbucket.familiar.extension.model.ShareModel

/**
 * @author JustBucket on 2019-07-24
 */
interface ShareRepository {

    suspend fun saveShareModel(shareModel: ShareModel): Either<Failure, Nothing>
}