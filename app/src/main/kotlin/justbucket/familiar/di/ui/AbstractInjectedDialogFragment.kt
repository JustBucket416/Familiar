package justbucket.familiar.di.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import justbucket.familiar.MainApplication
import justbucket.familiar.di.AppComponent
import justbucket.familiar.extension.resource.Resource
import justbucket.familiar.ui.viewmodel.BaseViewModel

/**
 * @author JustBucket on 2019-07-15
 */
abstract class AbstractInjectedDialogFragment<Data> : DialogFragment() {

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

    protected fun startObserving() {
        viewModel.getLiveData().observe(context as LifecycleOwner, Observer { resource ->
            resource?.let {
                handleDataState(it)
            }
        })
    }

    @CallSuper
    protected open fun resolveDependencies(component: AppComponent) {
        viewModelFactory = component.getViewModelFactory()
    }

    protected abstract fun setupForError(message: String?)

    protected abstract fun setupForSuccess(data: Data?)

    protected abstract fun setupForLoading()

    private fun handleDataState(resource: Resource<Data>) {
        when (resource) {
            is Resource.Loading -> setupForLoading()
            is Resource.Success -> setupForSuccess(resource.data)
            is Resource.Error -> setupForError(resource.errorMessage)
        }
    }
}