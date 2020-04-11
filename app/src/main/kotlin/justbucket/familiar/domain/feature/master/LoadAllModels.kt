package justbucket.familiar.domain.feature.master

import justbucket.familiar.domain.exception.Failure
import justbucket.familiar.domain.repository.MasterRepository
import justbucket.familiar.domain.usecase.UseCase
import justbucket.familiar.extension.model.MasterModel
import kotlin.coroutines.CoroutineContext

/**
 * @author JustBucket on 2019-07-12
 */
class LoadAllModels(
    context: CoroutineContext,
    private val repository: MasterRepository
) : UseCase<Pair<Failure?, List<MasterModel>>, Nothing?>(context) {

    override suspend fun run(params: Nothing?): Pair<Failure?, List<MasterModel>> {
        return repository.loadAllModels()
    }
}