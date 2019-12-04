package justbucket.familiar.content.extension

import android.view.View
import justbucket.familiar.content.extension.model.DetailModel
import justbucket.familiar.content.extension.model.MasterModel
import justbucket.familiar.content.extension.model.ShareModel

/**
 * @author JustBucket on 2019-07-22
 */
interface ExtensionConfigurator {

    val extensionName: String

    fun configureMasterModel(view: View, model: MasterModel)

    fun configureDetailModel(detailView: View, model: DetailModel)

    fun configureShareModel(shareView: View, model: ShareModel, shareSaver: (ShareModel) -> Unit)
}