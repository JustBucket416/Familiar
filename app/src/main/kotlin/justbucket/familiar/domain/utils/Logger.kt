package justbucket.familiar.domain.utils

import android.util.Log

/**
 * @author JustBucket on 2019-07-22
 */

var IS_LOGGING_ENABLED = false

fun logV(tag: String = TAG, message: String? = null, cause: Throwable? = null) = log(
    Level.VERBOSE,
    tag,
    message,
    cause
)

fun logD(tag: String = TAG, message: String? = null, cause: Throwable? = null) = log(
    Level.DEBUG,
    tag,
    message,
    cause
)

fun logI(tag: String = TAG, message: String? = null, cause: Throwable? = null) = log(
    Level.INFO,
    tag,
    message,
    cause
)

fun logW(tag: String = TAG, message: String? = null, cause: Throwable? = null) = log(
    Level.WARN,
    tag,
    message,
    cause
)

fun logE(tag: String = TAG, message: String? = null, cause: Throwable? = null) = log(
    Level.ERROR,
    tag,
    message,
    cause
)

fun logWTF(tag: String = TAG, message: String? = null, cause: Throwable? = null) = log(
    Level.WTF,
    tag,
    message,
    cause
)

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

private const val TAG = "Familiar"

private enum class Level {
    VERBOSE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
    WTF
}