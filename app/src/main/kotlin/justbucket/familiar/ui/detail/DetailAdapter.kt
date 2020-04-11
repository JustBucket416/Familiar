package justbucket.familiar.ui.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import justbucket.familiar.domain.extension.ExtensionManager
import justbucket.familiar.extension.model.MasterModel

class DetailAdapter(
    private val currentList: List<MasterModel>,
    supportFragmentManager: FragmentManager
) : FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        val masterModel = currentList[position]
        return ExtensionManager.getExtensions()[masterModel.extensionName]
            .configurator
            .configureDetailModel(masterModel) ?: Fragment()
    }

    override fun getCount() = currentList.size
}