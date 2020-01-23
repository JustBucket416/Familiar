package justbucket.familiar.domain.repository

import justbucket.familiar.extension.exception.Failure.FeatureFailure
import justbucket.familiar.extension.functional.Either
import justbucket.familiar.extension.model.SearchModel

/**
 * @author JustBucket on 2019-07-12
 */
interface SearchRepository {

    suspend fun searchByQuery(query: String): Either<FeatureFailure, Set<SearchModel>>
}