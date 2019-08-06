package justbucket.familiar.content.extension.activity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import justbucket.familiar.content.extension.constants.EXTENSION_SHARE_ACTION
import justbucket.familiar.content.extension.utils.Logger
import kotlin.system.exitProcess

/**
 * @author JustBucket on 2019-07-22
 */
open class UriHandlerActivity : Activity() {

    companion object {
        private const val message = "Could not start share activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val initialIntent = intent
        val intent = Intent(EXTENSION_SHARE_ACTION).setData(initialIntent.data)
        val extras = initialIntent.extras
        if (extras != null) {
            intent.putExtras(extras)
        }
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Logger.e(
                UriHandlerActivity::class.java.simpleName,
                message, e
            )
        }
        finish()
        exitProcess(0)
    }
}