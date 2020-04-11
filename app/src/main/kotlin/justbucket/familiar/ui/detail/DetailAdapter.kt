package justbucket.familiar.ui.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import justbucket.familiar.domain.extension.ExtensionManager
import justbucket.familiar.extension.model.MasterModel

class DetailAdapter(
    supportFragmentManager: FragmentManager
) : FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val currentList = ArrayList<MasterModel>()

    override fun getItem(position: Int): Fragment {
        val masterModel = currentList[position]
        return ExtensionManager.getExtensions()[masterModel.extensionName]
            .configurator
            .configureDetailModel(masterModel) ?: Fragment()
    }

    override fun getCount() = currentList.size

    fun updateList(newList: List<MasterModel>) {
        currentList.clear()
        currentList.addAll(newList)
        notifyDataSetChanged()
    }

}