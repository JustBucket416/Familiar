package justbucket.familiar.di.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.AndroidSupportInjection
import justbucket.familiar.di.viewmodel.ViewModelFactory
import justbucket.familiar.resource.Resource
import justbucket.familiar.resource.ResourceState
import justbucket.familiar.viewmodel.BaseViewModel
import javax.inject.Inject

/**
 * @author JustBucket on 2019-07-15
 */
abstract class AbstractInjectedDialogFragment<Data> : DialogFragment() {

    protected abstract val layoutId: Int
    protected abstract val viewModel: BaseViewModel<Data>
    protected lateinit var provider: ViewModelProvider
        private set

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        provider = ViewModelProviders.of(this, viewModelFactory)
        viewModel.liveData.observe(context as LifecycleOwner, Observer { resource ->
            resource?.let {
                handleDataState(it)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false)

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