package justbucket.familiar.domain.feature.share

import justbucket.familiar.domain.exception.Failure
import justbucket.familiar.domain.functional.Either
import justbucket.familiar.domain.repository.ShareRepository
import justbucket.familiar.domain.usecase.UseCase
import justbucket.familiar.extension.model.ShareModel
import kotlin.coroutines.CoroutineContext

/**
 * @author JustBucket on 2019-08-31
 */
class SaveShareModel(
    context: CoroutineContext,
    private val repository: ShareRepository
) : UseCase<Either<Failure, Nothing>, SaveShareModel.Params>(context) {

    override suspend fun run(params: Params?): Either<Failure, Nothing> {
        requireNotNull(params) { ILLEGAL_EXCEPTION_MESSAGE }
        return repository.saveShareModel(params.model)
    }

    data class Params internal constructor(val model: ShareModel) {
        companion object {
            fun createParams(model: ShareModel) = Params(model)
        }
    }
}