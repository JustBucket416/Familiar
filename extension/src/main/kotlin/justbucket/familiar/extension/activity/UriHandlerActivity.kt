package justbucket.familiar.extension.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import justbucket.familiar.extension.constants.EXTENSION_SHARE_ACTION
import kotlin.system.exitProcess

/**
 * @author JustBucket on 2019-07-22
 */
@SuppressLint("Registered")
open class UriHandlerActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val initialIntent = intent
        val intent = Intent(EXTENSION_SHARE_ACTION).setData(initialIntent.data)
        val extras = initialIntent.extras
        if (extras != null) {
            intent.putExtras(extras)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
            exit()
        } else {
            Toast.makeText(
                this, "You should install the core app to save the content",
                Toast.LENGTH_LONG
            ).show()
            Handler(mainLooper).postDelayed({ exit() }, Toast.LENGTH_LONG.toLong())
        }
    }

    private fun exit() {
        finish()
        exitProcess(0)
    }
}