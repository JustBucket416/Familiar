package justbucket.familiar.domain.feature.detail

import justbucket.familiar.content.extension.model.DetailModel
import justbucket.familiar.domain.exception.Failure
import justbucket.familiar.domain.functional.Either
import justbucket.familiar.domain.repository.DetailRepository
import justbucket.familiar.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * @author JustBucket on 2019-07-12
 */
class LoadModelDetails(
    context: CoroutineContext,
    private val repository: DetailRepository
) : UseCase<Either<Failure, DetailModel>, LoadModelDetails.Params>(context) {

    override suspend fun run(params: Params?): Either<Failure, DetailModel> {
        if (params == null) throw IllegalArgumentException(ILLEGAL_EXCEPTION_MESSAGE)
        return repository.loadModelDetails(params.modelId)
    }

    data class Params internal constructor(val modelId: Long) {
        companion object {
            fun createParams(modelId: Long) = Params(modelId)
        }
    }
}