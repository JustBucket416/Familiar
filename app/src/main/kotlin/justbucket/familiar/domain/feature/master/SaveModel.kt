package justbucket.familiar.domain.feature.master

import justbucket.familiar.domain.exception.Failure
import justbucket.familiar.domain.functional.Either
import justbucket.familiar.domain.repository.MasterRepository
import justbucket.familiar.domain.usecase.UseCase
import justbucket.familiar.extension.model.MasterModel
import kotlin.coroutines.CoroutineContext

class SaveModel(
    context: CoroutineContext,
    private val repository: MasterRepository
) : UseCase<Either<Failure.DBFailure, Long>, SaveModel.Params>(context) {

    override suspend fun run(params: Params?): Either<Failure.DBFailure, Long> {
        requireNotNull(params) { ILLEGAL_EXCEPTION_MESSAGE }
        return repository.saveModel(params.masterModel)
    }

    data class Params internal constructor(val masterModel: MasterModel) {
        companion object {
            fun createParams(masterModel: MasterModel) = Params(masterModel)
        }
    }
}