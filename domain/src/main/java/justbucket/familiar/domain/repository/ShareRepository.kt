package justbucket.familiar.domain.repository

import justbucket.familiar.content.extension.model.ShareModel

/**
 * @author JustBucket on 2019-07-24
 */
interface ShareRepository {

    suspend fun addShareModel(shareModel: ShareModel)
}