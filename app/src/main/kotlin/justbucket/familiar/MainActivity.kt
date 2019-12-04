package justbucket.familiar

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import justbucket.familiar.content.extension.model.MasterModel
import justbucket.familiar.di.ui.AbstractInjectedActivity
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
        initViews()
        viewModel.loadModels()
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
        content_recycler.setExpandablePage(expandable_layout)
    }
}