package justbucket.familiar.domain.feature.master

import justbucket.familiar.content.extension.model.MasterModel
import justbucket.familiar.domain.exception.Failure
import justbucket.familiar.domain.functional.Either
import justbucket.familiar.domain.repository.MasterRepository
import justbucket.familiar.domain.usecase.UseCase
import kotlin.coroutines.CoroutineContext

/**
 * @author JustBucket on 2019-07-12
 */
class LoadAllModels(
    context: CoroutineContext,
    private val repository: MasterRepository
) : UseCase<Either<Failure.DBFailure, Set<MasterModel>>, Nothing>(context) {

    override suspend fun run(params: Nothing?): Either<Failure.DBFailure, Set<MasterModel>> {
        return repository.loadAllModels()
    }
}