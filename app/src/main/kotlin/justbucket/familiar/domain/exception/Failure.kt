package justbucket.familiar.domain.exception

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
sealed class Failure(open val errorMessage: String, open val throwable: Throwable? = null) {
    class NetworkFailure(override val errorMessage: String, override val throwable: Throwable?) :
        Failure(errorMessage, throwable)

    class DBFailure(override val errorMessage: String, override val throwable: Throwable?) :
        Failure(errorMessage, throwable)

    /** * Extend this class for feature specific failures.*/
    open class FeatureFailure(override val errorMessage: String = "Feature failure", override val throwable: Throwable? = null) :
        Failure(errorMessage, throwable)
}
