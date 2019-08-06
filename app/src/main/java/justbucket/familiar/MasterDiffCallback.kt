package justbucket.familiar

import androidx.recyclerview.widget.DiffUtil
import justbucket.familiar.content.extension.model.MasterModel

/**
 * @author JustBucket on 2019-07-30
 */
class MasterDiffCallback : DiffUtil.ItemCallback<MasterModel>() {
    override fun areItemsTheSame(oldItem: MasterModel, newItem: MasterModel) =
        oldItem::class.java == newItem::class.java

    override fun areContentsTheSame(oldItem: MasterModel, newItem: MasterModel) = oldItem.id == newItem.id
}