package justbucket.familiar.ui.master

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import justbucket.familiar.R
import justbucket.familiar.extension.model.MasterModel
import kotlinx.android.synthetic.main.view_holder_master.view.*

/**
 * @author JustBucket on 2019-07-30
 */
class MasterAdapter(
    private val onClickListener: (MasterModel) -> Unit,
    private val onLongClickListener: (MasterModel) -> Unit
) : ListAdapter<MasterModel, MasterAdapter.MasterHolder>(
    MasterDiffCallback()
) {

    init {
        setHasStableIds(true)
    }

    override fun getItemViewType(position: Int) = getItem(position).extensionName.hashCode()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasterHolder {
        return LayoutInflater
            .from(parent.context)
            .inflate(R.layout.view_holder_master, parent, false)
            .let { MasterHolder(it) }
    }

    override fun onBindViewHolder(holder: MasterHolder, position: Int) =
        getItem(position).let { holder.bind(it, onClickListener, onLongClickListener) }

    override fun getItemId(position: Int) = getItem(position).id

    class MasterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            model: MasterModel, clickListener: (MasterModel) -> Unit,
            longClickListener: (MasterModel) -> Unit
        ) {
            itemView.name_text_view.text = model.title
            itemView.description_text_view.text = model.description
            Glide.with(itemView.context).load(model.imageLink).into(itemView.image_view)

            itemView.setOnClickListener {
                clickListener.invoke(model)
            }

            itemView.setOnLongClickListener {
                longClickListener.invoke(model)
                true
            }
        }
    }
}