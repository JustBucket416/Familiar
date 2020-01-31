package justbucket.familiar.domain.feature.detail

import justbucket.familiar.domain.repository.DetailRepository
import justbucket.familiar.domain.usecase.UseCase
import justbucket.familiar.extension.exception.Failure
import justbucket.familiar.extension.functional.Either
import justbucket.familiar.extension.model.DetailModel
import kotlin.coroutines.CoroutineContext

/**
 * @author JustBucket on 2019-07-12
 */
class LoadModelDetails(
    context: CoroutineContext,
    private val repository: DetailRepository
) : UseCase<Either<Failure, DetailModel>, LoadModelDetails.Params>(context) {

    override suspend fun run(params: Params?): Either<Failure, DetailModel> {
        requireNotNull(params) { ILLEGAL_EXCEPTION_MESSAGE }
        return repository.loadModelDetails(params.modelId)
    }

    data class Params internal constructor(val modelId: Long) {
        companion object {
            fun createParams(modelId: Long) = Params(modelId)
        }
    }
}