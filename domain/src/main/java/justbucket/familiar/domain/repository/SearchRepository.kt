package justbucket.familiar.domain.repository

import justbucket.familiar.content.extension.model.SearchModel
import justbucket.familiar.content.extension.model.ShareModel
import justbucket.familiar.domain.exception.Failure
import justbucket.familiar.domain.functional.Either

/**
 * @author JustBucket on 2019-07-12
 */
interface SearchRepository {

    suspend fun searchByQuery(query: String): Either<Failure.NetworkFailure, Set<SearchModel>>

    suspend fun addContentEntry(searchModel: SearchModel): Either<Failure, Long>
}