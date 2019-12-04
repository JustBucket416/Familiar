package justbucket.familiar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import justbucket.familiar.content.extension.model.MasterModel

/**
 * @author JustBucket on 2019-07-30
 */
class MasterAdapter : ListAdapter<MasterModel, MasterAdapter.MasterHolder>(MasterDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun getItemViewType(position: Int) = getItem(position).extensionName.hashCode()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterHolder {
        return MasterHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_holder_master, parent, false))
    }

    override fun onBindViewHolder(holder: MasterHolder, position: Int) {
        val model = getItem(position)
        holder.itemView.name_text_view.text = model.title
        holder.itemView.description_text_view.text = model.description
        Glide.with(holder.itemView.context).load(model.imagePath).into(holder.itemView.image_view)
    }

    override fun getItemId(position: Int) = getItem(position).id

    class MasterHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}