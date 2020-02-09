package justbucket.familiar

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import justbucket.familiar.di.ui.AbstractInjectedActivity
import justbucket.familiar.extension.model.MasterModel
import justbucket.familiar.viewmodel.MasterViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AbstractInjectedActivity<Set<MasterModel>>() {

    private lateinit var masterAdapter: MasterAdapter

    override val viewModel: MasterViewModel
        get() = provider[MasterViewModel::class.java]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        masterAdapter = MasterAdapter()
        viewModel.loadModels()
        initViews()
        startObserving()
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
                    viewModel.loadContent(s)
                    return true
                }
            })
        }

        return super.onPrepareOptionsMenu(menu)
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
        expandable_layout.visibility = View.VISIBLE
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

    private fun initViews() {
        content_recycler.layoutManager = LinearLayoutManager(this)
        content_recycler.adapter = masterAdapter
        content_recycler.expandablePage = expandable_layout
    }
}