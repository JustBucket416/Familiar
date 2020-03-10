package justbucket.familiar

import android.os.Bundle
import android.view.GestureDetector
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.LinearLayoutManager
import justbucket.familiar.di.ui.AbstractInjectedActivity
import justbucket.familiar.domain.extension.ExtensionManager
import justbucket.familiar.extension.model.MasterModel
import justbucket.familiar.viewmodel.MasterViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AbstractInjectedActivity<Set<MasterModel>>() {

    private lateinit var masterAdapter: MasterAdapter
    private var query = ""

    override val viewModel: MasterViewModel
        get() = provider[MasterViewModel::class.java]

    override fun onCreate(savedInstanceState: Bundle?) {
        ExtensionManager.loadExtensions(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        masterAdapter = MasterAdapter { viewModel.saveModel(it) }
        viewModel.loadModels()
        initViews()
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

    private fun initViews() {
        content_recycler.layoutManager = LinearLayoutManager(this)
        content_recycler.adapter = masterAdapter
        content_recycler.expandablePage = expandable_layout

        val gestureDetectorCompat =
            GestureDetectorCompat(this, object : GestureDetector.OnGestureListener {
                override fun onShowPress(e: MotionEvent?) {

                }

                override fun onSingleTapUp(e: MotionEvent?): Boolean {
                    return false
                }

                override fun onDown(e: MotionEvent?): Boolean {
                    return false
                }

                override fun onFling(
                    e1: MotionEvent?,
                    e2: MotionEvent?,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    Toast.makeText(this@MainActivity, "Reloading extensions", Toast.LENGTH_SHORT)
                        .show()
                    ExtensionManager.loadExtensions(this@MainActivity)
                    return true
                }

                override fun onScroll(
                    e1: MotionEvent?,
                    e2: MotionEvent?,
                    distanceX: Float,
                    distanceY: Float
                ): Boolean {
                    return false
                }

                override fun onLongPress(e: MotionEvent?) {

                }
            })
        (content_recycler.parent as View).setOnTouchListener { _, event ->
            gestureDetectorCompat.onTouchEvent(event)
            true
        }
    }
}