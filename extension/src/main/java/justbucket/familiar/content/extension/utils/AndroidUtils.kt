package justbucket.familiar.content.extension.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * @author JustBucket on 2019-07-22
 */
object AndroidUtils {
    interface OnReceiveListener {
        fun onReceive(receiver: BroadcastReceiver, context: Context, intent: Intent)
    }

    fun createReceiver(listener: OnReceiveListener): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                listener.onReceive(this, context, intent)
            }
        }
    }
}