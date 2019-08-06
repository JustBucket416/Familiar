package justbucket.familiar.di.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.android.AndroidInjection
import justbucket.familiar.di.viewmodel.ViewModelFactory
import justbucket.familiar.resource.Resource
import justbucket.familiar.resource.ResourceState
import justbucket.familiar.viewmodel.BaseViewModel
import javax.inject.Inject

/**
 * @author JustBucket on 2019-07-30
 */
abstract class AbstractInjectedActivity<Data, T : BaseViewModel<Data>> : AppCompatActivity() {

    protected abstract val viewModel: T

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        viewModel.liveData.observe(this, Observer { resource ->
            resource?.let {
                handleDataState(it)
            }
        })
    }

    private fun handleDataState(resource: Resource<Data>) {
        when (resource.status) {
            ResourceState.LOADING -> setupForLoading()
            ResourceState.SUCCESS -> setupForSuccess(resource.data)
            ResourceState.ERROR -> setupForError(resource.message)
        }
    }

    abstract fun setupForError(message: String?)

    abstract fun setupForSuccess(data: Data?)

    abstract fun setupForLoading()
}