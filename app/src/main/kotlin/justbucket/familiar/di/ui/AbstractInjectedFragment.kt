package justbucket.familiar.di.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import justbucket.familiar.MainApplication
import justbucket.familiar.di.AppComponent
import justbucket.familiar.resource.Resource
import justbucket.familiar.resource.ResourceState
import justbucket.familiar.viewmodel.BaseViewModel

/**
 * @author JustBucket on 2019-07-15
 */
abstract class AbstractInjectedFragment<Data> : Fragment() {

    protected abstract val layoutId: Int
    protected abstract val viewModel: BaseViewModel<Data>
    protected val provider: ViewModelProvider = ViewModelProvider(this, viewModelFactory)

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context) {
        resolveDependencies((context.applicationContext as MainApplication).component)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false)

    @CallSuper
    protected open fun resolveDependencies(component: AppComponent) {
        viewModelFactory = component.getViewModelFactory()
    }

    protected fun startObserving() {
        viewModel.getLiveData().observe(context as LifecycleOwner, Observer { resource ->
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
}