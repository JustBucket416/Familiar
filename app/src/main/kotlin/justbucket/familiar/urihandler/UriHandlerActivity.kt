package justbucket.familiar.urihandler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import justbucket.familiar.R
import justbucket.familiar.di.viewmodel.ViewModelFactory
import justbucket.familiar.domain.extension.ExtensionManager
import justbucket.familiar.viewmodel.UriHandlerModel

/**
 * @author JustBucket on 2019-08-28
 */
class UriHandlerActivity : AppCompatActivity() {

    private val viewModel: UriHandlerModel
        get() = provider[UriHandlerModel::class.java]

    private val provider = ViewModelProvider(this)

    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uri_handler)
        val uri = requireNotNull(intent.data)

        val extensionHolder = ExtensionManager.getExtensions()[requireNotNull(uri.authority)]
        val creator = extensionHolder.creator

        val shareModel = creator.createShareModel(uri.toString())
        extensionHolder.configurator.configureShareModel(findViewById(R.id.share_view), shareModel) {
            viewModel.saveModel(it)
        }
    }
}