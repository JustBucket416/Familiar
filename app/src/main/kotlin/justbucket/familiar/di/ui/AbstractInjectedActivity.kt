package justbucket.familiar.di.ui

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import justbucket.familiar.MainApplication
import justbucket.familiar.di.AppComponent
import justbucket.familiar.resource.Resource
import justbucket.familiar.resource.ResourceState
import justbucket.familiar.viewmodel.BaseViewModel

/**
 * @author JustBucket on 2019-07-30
 */
abstract class AbstractInjectedActivity<Data> : AppCompatActivity() {

    protected abstract val viewModel: BaseViewModel<Data>
    protected val provider = ViewModelProvider(this, viewModelFactory)

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        resolveDependencies((application as MainApplication).component)
        super.onCreate(savedInstanceState)

        viewModel.getLiveData().observe(this, Observer { resource ->
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

    protected abstract fun setupForError(message: String?)

    protected abstract fun setupForSuccess(data: Data?)

    protected abstract fun setupForLoading()

    @CallSuper
    protected fun resolveDependencies(component: AppComponent) {
        viewModelFactory = component.getViewModelFactory()
    }
}