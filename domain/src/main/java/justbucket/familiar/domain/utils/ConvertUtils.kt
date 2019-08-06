package justbucket.familiar.domain.utils

import justbucket.familiar.domain.exception.ConvertException

@Suppress("NOTHING_TO_INLINE")
inline fun <T> T?.getOrDie(binding: String): T = this
    ?: throw ConvertException("'$binding' must not be null")