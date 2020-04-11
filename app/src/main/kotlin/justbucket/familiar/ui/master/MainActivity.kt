package justbucket.familiar.ui.master

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import justbucket.familiar.R
import justbucket.familiar.di.ui.AbstractInjectedActivity
import justbucket.familiar.domain.extension.ExtensionManager
import justbucket.familiar.extension.fragment.DetailFragment
import justbucket.familiar.extension.model.DetailModel
import justbucket.familiar.extension.model.MasterModel
import justbucket.familiar.extension.resource.Resource
import justbucket.familiar.ui.detail.DetailAdapter
import justbucket.familiar.ui.viewmodel.MasterViewModel
import justbucket.familiar.utils.NonNullMap
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.saket.inboxrecyclerview.InboxRecyclerView
import me.saket.inboxrecyclerview.locationOnScreen

@ExperimentalCoroutinesApi
class MainActivity : AbstractInjectedActivity<List<MasterModel>>(),
    DetailFragment.FragmentProvider {

    private val locationBuffer = IntArray(2)
    private val positionMap = NonNullMap<Int, InboxRecyclerView.ExpandedItem>()
    private lateinit var masterAdapter: MasterAdapter
    private var query = ""
    private var previousPosition = 0

    override val viewModel: MasterViewModel
        get() = provider[MasterViewModel::class.java]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ExtensionManager.loadExtensions(this)
        setContentView(R.layout.activity_main)
        masterAdapter =
            MasterAdapter(::openDetailModel) {
                viewModel.saveModel(it)
            }
        viewModel.loadModels()
        initViews()
        setupToolbar()
        startObserving()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            viewModel.loadContent(query)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val view = menu.findItem(R.id.qps_search_item)?.actionView
        if (view is SearchView) {
            view.queryHint = getString(R.string.query_hint)
            view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(s: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(s: String): Boolean {
                    query = s
                    viewModel.loadContent(s)
                    return true
                }
            })
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if (expandable_layout.isExpanded) {
            content_recycler.collapse()
        } else {
            super.onBackPressed()
        }
    }

    override fun setupForError(message: String?) {
        content_recycler.visibility = View.GONE
        expandable_layout.visibility = View.GONE
        progress_bar.visibility = View.GONE
        error_text_view.visibility = View.VISIBLE

        error_text_view.text = message ?: getString(R.string.error_loading_master_models)
    }

    override fun setupForSuccess(data: List<MasterModel>?) {
        content_recycler.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE
        error_text_view.visibility = View.GONE

        if (!data.isNullOrEmpty()) {
            masterAdapter.submitList(data)
            view_pager.adapter = DetailAdapter(data, supportFragmentManager)
        } else {
            setupForError(getString(R.string.error_no_content_found))
        }
    }

    override fun setupForLoading() {
        content_recycler.visibility = View.GONE
        expandable_layout.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE
        error_text_view.visibility = View.GONE
    }

    override fun getThemedLayoutInflater(extensionName: String): LayoutInflater {
        return ExtensionManager.getExtensions()[extensionName].configurator.themedAppContext
            .let { LayoutInflater.from(it) }
    }

    override fun loadDetailModel(masterModel: MasterModel): LiveData<Resource<DetailModel>> {
        return viewModel.loadDetails(masterModel)
    }

    override fun onDestroy() {
        super.onDestroy()
        view_pager.clearOnPageChangeListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        setTitle(R.string.app_name)
    }

    private fun initViews() {
        content_recycler.layoutManager = LinearLayoutManager(this)
        content_recycler.adapter = masterAdapter
        content_recycler.expandablePage = expandable_layout

        view_pager.addOnPageChangeListener(DetailAdapterPageChangeListener())
    }

    private fun openDetailModel(model: MasterModel) {
        previousPosition = masterAdapter.currentList.indexOfFirst { it.id == model.id }
        fillExpandedItems()
        view_pager.setCurrentItem(
            previousPosition,
            false
        )
        content_recycler.expandItem(model.id)
    }

    private inner class DetailAdapterPageChangeListener : ViewPager.SimpleOnPageChangeListener() {

        override fun onPageSelected(position: Int) {
            content_recycler.findViewHolderForAdapterPosition(previousPosition)?.itemView?.alpha =
                1F
            content_recycler.expandedItem = positionMap[position]

            content_recycler.itemExpandAnimator.onPageMove(
                content_recycler,
                content_recycler.expandablePage!!
            )
            previousPosition = position
        }
    }

    private fun fillExpandedItems() {
        positionMap.clear()
        masterAdapter.currentList.forEachIndexed { index, masterModel ->
            val item = content_recycler.makeExpandedItem(index, masterModel.id)
            positionMap[index] = item
        }
    }

    private fun RecyclerView.makeExpandedItem(
        position: Int,
        itemId: Long
    ): InboxRecyclerView.ExpandedItem {
        val itemView: View = (layoutManager as LinearLayoutManager).findViewByPosition(position)
            ?: return InboxRecyclerView.ExpandedItem(
                adapterId = -1,
                viewIndex = -1,
                locationOnScreen = Rect(0, 0, 0, 0)
            )

        val itemViewPosition = indexOfChild(itemView)
        val itemRect = itemView.locationOnScreen(locationBuffer)

        return InboxRecyclerView.ExpandedItem(itemViewPosition, itemId, itemRect)
    }

}