package justbucket.familiar.urihandler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.AndroidInjection
import justbucket.familiar.R
import justbucket.familiar.di.viewmodel.ViewModelFactory
import justbucket.familiar.domain.extension.ExtensionManager
import justbucket.familiar.viewmodel.UriHandlerModel
import javax.inject.Inject

/**
 * @author JustBucket on 2019-08-28
 */
class UriHandlerActivity : AppCompatActivity() {

    private val viewModel: UriHandlerModel
        get() = provider[UriHandlerModel::class.java]
    private val exManager = ExtensionManager.getInstance(this)

    private lateinit var provider: ViewModelProvider

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uri_handler)
        AndroidInjection.inject(this)
        provider = ViewModelProviders.of(this, viewModelFactory)
        val uri = intent.data

        val extensionHolder = exManager.getExtensions()[uri.authority]
        val creator = extensionHolder.creator

        val shareModel = creator.createShareModel(uri.toString())
        extensionHolder.configurator.configureShareModel(findViewById(R.id.share_view), shareModel) {
            viewModel.saveModel(it)
        }
    }
}