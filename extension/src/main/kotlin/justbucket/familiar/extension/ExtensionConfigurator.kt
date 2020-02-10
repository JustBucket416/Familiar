package justbucket.familiar.extension

import android.content.Context
import android.view.View
import justbucket.familiar.extension.model.DetailModel
import justbucket.familiar.extension.model.MasterModel
import justbucket.familiar.extension.model.ShareModel

/**
 * @author JustBucket on 2019-07-22
 */
interface ExtensionConfigurator {

    val extensionName: String

    var context: Context?

    fun configureMasterModel(view: View, model: MasterModel)

    fun configureDetailModel(detailView: View, model: DetailModel)

    fun configureShareModel(shareView: View, model: ShareModel, shareSaver: (ShareModel) -> Unit)
}