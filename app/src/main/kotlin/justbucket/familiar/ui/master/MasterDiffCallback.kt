package justbucket.familiar.ui.master

import androidx.recyclerview.widget.DiffUtil
import justbucket.familiar.extension.model.MasterModel

/**
 * @author JustBucket on 2019-07-30
 */
class MasterDiffCallback : DiffUtil.ItemCallback<MasterModel>() {
    override fun areItemsTheSame(oldItem: MasterModel, newItem: MasterModel) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MasterModel, newItem: MasterModel): Boolean {
        return oldItem.title == newItem.title &&
                oldItem.extensionName == newItem.extensionName &&
                oldItem.description == newItem.description &&
                oldItem.imageLink == newItem.imageLink &&
                oldItem.detailViewLink == newItem.detailViewLink
    }
}