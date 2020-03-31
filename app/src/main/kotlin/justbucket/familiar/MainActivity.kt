package justbucket.familiar

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import justbucket.familiar.di.ui.AbstractInjectedActivity
import justbucket.familiar.domain.extension.ExtensionManager
import justbucket.familiar.extension.model.MasterModel
import justbucket.familiar.resource.Resource
import justbucket.familiar.viewmodel.MasterViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AbstractInjectedActivity<Set<MasterModel>>() {

    private lateinit var masterAdapter: MasterAdapter
    private var query = ""

    override val viewModel: MasterViewModel
        get() = provider[MasterViewModel::class.java]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ExtensionManager.loadExtensions(this)
        setContentView(R.layout.activity_main)
        masterAdapter = MasterAdapter(::openDetailModel) { viewModel.saveModel(it) }
        viewModel.loadModels()
        initViews()
        setupToolbar()
        startObserving()
        subscribeToDetails()
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

    override fun setupForSuccess(data: Set<MasterModel>?) {
        content_recycler.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE
        error_text_view.visibility = View.GONE

        if (!data.isNullOrEmpty()) {
            masterAdapter.submitList(data.toList())
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

    private fun subscribeToDetails() {
        viewModel.getDetailModelData().observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    expandable_layout.detail_progress_bar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    expandable_layout.detail_progress_bar.visibility = View.GONE
                    detail_layout.removeAllViews()
                    ExtensionManager.getExtensions()[it.data.extensionName].configurator
                        .configureDetailModel(detail_layout, it.data)
                }
                is Resource.Error -> {
                    expandable_layout.detail_progress_bar.visibility = View.GONE
                    content_recycler.collapse()
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        setTitle(R.string.app_name)
    }

    private fun initViews() {
        content_recycler.layoutManager = LinearLayoutManager(this)
        content_recycler.adapter = masterAdapter
        content_recycler.expandablePage = expandable_layout
    }

    private fun openDetailModel(model: MasterModel) {
        content_recycler.expandItem(model.id)
        viewModel.loadDetails(model)
    }
}