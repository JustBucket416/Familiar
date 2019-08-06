package justbucket.familiar.content.extension.utils

import android.util.Log
import justbucket.familiar.content.extension.BuildConfig

/**
 * @author JustBucket on 2019-07-22
 */
object Logger {

    var IS_LOGGING_ENABLED = BuildConfig.DEBUG

    fun v(tag: String, message: String? = null, cause: Throwable? = null) {
        log(
                Level.VERBOSE,
                tag,
                message,
                cause
        )
    }

    fun d(tag: String, message: String? = null, cause: Throwable? = null) {
        log(
                Level.DEBUG,
                tag,
                message,
                cause
        )
    }

    fun i(tag: String, message: String? = null, cause: Throwable? = null) {
        log(
                Level.INFO,
                tag,
                message,
                cause
        )
    }

    fun w(tag: String, message: String? = null, cause: Throwable? = null) {
        log(
                Level.WARN,
                tag,
                message,
                cause
        )
    }

    fun e(tag: String, message: String? = null, cause: Throwable? = null) {
        log(
                Level.ERROR,
                tag,
                message,
                cause
        )
    }

    fun wtf(tag: String, message: String? = null, cause: Throwable? = null) {
        log(
                Level.WTF,
                tag,
                message,
                cause
        )
    }

    private fun log(level: Level, tag: String, message: String? = null, cause: Throwable? = null) {
        if (IS_LOGGING_ENABLED) {
            when (level) {
                Level.VERBOSE -> Log.v(tag, message, cause)
                Level.DEBUG -> Log.d(tag, message, cause)
                Level.INFO -> Log.i(tag, message, cause)
                Level.WARN -> Log.w(tag, message, cause)
                Level.ERROR -> Log.e(tag, message, cause)
                Level.WTF -> Log.wtf(tag, message, cause)
            }
        }
    }

    private enum class Level {
        VERBOSE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
        WTF
    }
}