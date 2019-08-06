package justbucket.familiar.domain.feature.master

import justbucket.familiar.domain.exception.Failure
import justbucket.familiar.domain.functional.Either
import justbucket.familiar.domain.repository.MasterRepository
import justbucket.familiar.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * @author JustBucket on 2019-07-12
 */
class DeleteModel(
    context: CoroutineContext,
    private val repository: MasterRepository
) : UseCase<Either<Failure.DBFailure, Long>, DeleteModel.Params>(context) {

    override suspend fun run(params: Params?): Either<Failure.DBFailure, Long> {
        if (params == null) throw IllegalArgumentException(ILLEGAL_EXCEPTION_MESSAGE)
        return repository.deleteModel(params.modelId)
    }

    data class Params internal constructor(val modelId: Long) {
        companion object {
            fun createParams(modelId: Long) = Params(modelId)
        }
    }
}