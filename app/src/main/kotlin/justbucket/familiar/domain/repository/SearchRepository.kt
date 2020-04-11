package justbucket.familiar.domain.repository

import justbucket.familiar.extension.model.MasterModel

/**
 * @author JustBucket on 2019-07-12
 */
interface SearchRepository {

    suspend fun searchByQuery(query: String): List<MasterModel>
}