package justbucket.familiar.content.extension

import android.net.Uri
import justbucket.familiar.content.extension.model.SearchModel
import justbucket.familiar.content.extension.model.ShareModel

/**
 * @author JustBucket on 2019-07-24
 */
abstract class ExtensionLocator {

    abstract val extensionName: String

    abstract fun getSearchModelsByQuery(query: String): Uri

    abstract fun getMasterForShare(shareModel: ShareModel): Uri

    abstract fun getMasterForSearch(searchModel: SearchModel): Uri

    abstract fun getDetailsForShare(shareModel: ShareModel): Uri

    abstract fun getDetailsForSearch(searchModel: SearchModel): Uri
}