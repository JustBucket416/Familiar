package justbucket.familiar.content.extension

import justbucket.familiar.content.extension.model.DetailModel
import justbucket.familiar.content.extension.model.MasterModel
import justbucket.familiar.content.extension.model.SearchModel
import justbucket.familiar.content.extension.model.ShareModel

/**
 * @author JustBucket on 2019-07-22
 */
abstract class ExtensionModelCreator {

    abstract val extensionName: String

    abstract fun createMasterModel(jsonString: String): MasterModel

    abstract fun createDetailModel(jsonString: String): DetailModel

    abstract fun createSearchModel(jsonString: String): Set<SearchModel>

    abstract fun createShareModel(shareDate: String): ShareModel

}