package justbucket.familiar.domain.feature.search

import justbucket.familiar.domain.repository.SearchRepository
import justbucket.familiar.domain.usecase.UseCase
import justbucket.familiar.extension.exception.Failure.FeatureFailure
import justbucket.familiar.extension.functional.Either
import justbucket.familiar.extension.model.SearchModel
import kotlin.coroutines.CoroutineContext

/**
 * @author JustBucket on 2019-07-12
 */
class SearchByQuery(
    context: CoroutineContext,
    private val repository: SearchRepository
) : UseCase<Either<FeatureFailure, Set<SearchModel>>, SearchByQuery.Params>(context) {

    public override suspend fun run(params: Params?): Either<FeatureFailure, Set<SearchModel>> {
        requireNotNull(params) { ILLEGAL_EXCEPTION_MESSAGE }
        return repository.searchByQuery(params.query)
    }

    data class Params internal constructor(val query: String) {
        companion object {
            fun createParams(query: String) = Params(query)
        }
    }
}