package justbucket.familiar.domain.feature.detail

import justbucket.familiar.domain.exception.Failure
import justbucket.familiar.domain.functional.Either
import justbucket.familiar.domain.repository.DetailRepository
import justbucket.familiar.domain.usecase.UseCase
import justbucket.familiar.extension.model.DetailModel
import justbucket.familiar.extension.model.MasterModel
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
        return repository.loadModelDetails(params.masterModel)
    }

    data class Params internal constructor(val masterModel: MasterModel) {
        companion object {
            fun createParams(masterModel: MasterModel) = Params(masterModel)
        }
    }
}