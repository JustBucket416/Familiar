package justbucket.familiar.domain.feature.search

import justbucket.familiar.content.extension.model.SearchModel
import justbucket.familiar.domain.exception.Failure
import justbucket.familiar.domain.functional.Either
import justbucket.familiar.domain.repository.SearchRepository
import justbucket.familiar.domain.usecase.UseCase
import kotlin.coroutines.CoroutineContext

/**
 * @author JustBucket on 2019-07-12
 */
class SearchByQuery(
    context: CoroutineContext,
    private val repository: SearchRepository
) : UseCase<Either<Failure.NetworkFailure, Set<SearchModel>>, SearchByQuery.Params>(context) {

    override suspend fun run(params: Params?): Either<Failure.NetworkFailure, Set<SearchModel>> {
        if (params == null) throw IllegalArgumentException(ILLEGAL_EXCEPTION_MESSAGE)
        return repository.searchByQuery(params.query)
    }

    data class Params internal constructor(val query: String) {
        companion object {
            fun createParams(query: String) = Params(query)
        }
    }
}