package justbucket.familiar.content.extension

import android.view.View
import justbucket.familiar.content.extension.model.DetailModel
import justbucket.familiar.content.extension.model.MasterModel
import justbucket.familiar.content.extension.model.ShareModel

/**
 * @author JustBucket on 2019-07-22
 */
abstract class ExtensionConfigurator {

    abstract val extensionName: String

    abstract fun configureMasterModel(view: View, model: MasterModel)

    abstract fun configureDetailModel(detailView: View, model: DetailModel)

    abstract fun configureShareModel(shareView: View, model: ShareModel)
}