package justbucket.familiar.content.extension

/**
 * @author JustBucket on 2019-07-22
 */
open class ApiException : Exception() {

    companion object {
        init {
            if (true) {
                throw IllegalAccessError()
            }
        }
    }
}