package justbucket.familiar.domain.feature.master

import justbucket.familiar.domain.exception.Failure
import justbucket.familiar.domain.functional.Either
import justbucket.familiar.domain.functional.flatMap
import justbucket.familiar.domain.repository.DetailRepository
import justbucket.familiar.domain.repository.MasterRepository
import justbucket.familiar.domain.usecase.UseCase
import justbucket.familiar.extension.model.MasterModel
import kotlin.coroutines.CoroutineContext

class SaveModel(
    context: CoroutineContext,
    private val masterRepository: MasterRepository,
    private val detailRepository: DetailRepository
) : UseCase<Either<Failure, Long>, SaveModel.Params>(context) {

    override suspend fun run(params: Params?): Either<Failure, Long> {
        requireNotNull(params) { ILLEGAL_EXCEPTION_MESSAGE }
        val id = masterRepository.saveModel(params.masterModel)
        return detailRepository.loadModelDetails(params.masterModel)
            .flatMap { detailModel -> detailRepository.saveDetailModel(detailModel) }
            .flatMap { id }
    }

    data class Params internal constructor(val masterModel: MasterModel) {
        companion object {
            fun createParams(masterModel: MasterModel) = Params(masterModel)
        }
    }
}