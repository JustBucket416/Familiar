package justbucket.familiar.domain.repository

import justbucket.familiar.content.extension.exception.Failure
import justbucket.familiar.content.extension.functional.Either
import justbucket.familiar.content.extension.model.SearchModel

/**
 * @author JustBucket on 2019-07-12
 */
interface SearchRepository {

    suspend fun searchByQuery(query: String): Either<Failure.FeatureFailure, Set<SearchModel>>
}