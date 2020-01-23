package justbucket.familiar.domain.feature.master

import justbucket.familiar.domain.repository.MasterRepository
import justbucket.familiar.domain.usecase.UseCase
import justbucket.familiar.extension.exception.Failure.DBFailure
import justbucket.familiar.extension.functional.Either
import kotlin.coroutines.CoroutineContext

/**
 * @author JustBucket on 2019-07-12
 */
class DeleteModel(
    context: CoroutineContext,
    private val repository: MasterRepository
) : UseCase<Either<DBFailure, Long>, DeleteModel.Params>(context) {

    override suspend fun run(params: Params?): Either<DBFailure, Long> {
        requireNotNull(params) { ILLEGAL_EXCEPTION_MESSAGE }
        return repository.deleteModel(params.modelId)
    }

    data class Params internal constructor(val modelId: Long) {
        companion object {
            fun createParams(modelId: Long) = Params(modelId)
        }
    }
}