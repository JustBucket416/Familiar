package justbucket.familiar.urihandler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import justbucket.familiar.MainApplication
import justbucket.familiar.R
import justbucket.familiar.di.AppComponent
import justbucket.familiar.domain.extension.ExtensionManager
import justbucket.familiar.extension.model.ShareModel
import justbucket.familiar.viewmodel.UriHandlerModel

/**
 * @author JustBucket on 2019-08-28
 */
class UriHandlerActivity : AppCompatActivity() {

    private val viewModel: UriHandlerModel
        get() = provider[UriHandlerModel::class.java]

    private lateinit var provider: ViewModelProvider
    private lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        resolveDependencies((application as MainApplication).component)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uri_handler)
        val uri = requireNotNull(intent.data)

        val extensionHolder = ExtensionManager.getExtensions()[requireNotNull(uri.authority)]
        val creator = extensionHolder.mapper

        val shareModel = creator.createShareModel(uri) ?: ShareModel(
            requireNotNull(uri.authority),
            requireNotNull(uri.host)
        )
        val alreadyConfigured = extensionHolder.configurator.configureShareModel(
            findViewById(R.id.share_view),
            shareModel
        ) {
            viewModel.saveModel(it)
        }
        if (!alreadyConfigured) {
            configureShareModel(shareModel)
        }
    }

    private fun resolveDependencies(component: AppComponent) {
        viewModelFactory = component.getViewModelFactory()
        provider = ViewModelProvider(this, viewModelFactory)
    }

    private fun configureShareModel(shareModel: ShareModel) {
        TODO()
    }
}