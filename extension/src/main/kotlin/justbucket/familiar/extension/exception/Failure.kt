package justbucket.familiar.content.extension.exception

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
    abstract class FeatureFailure(override val errorMessage: String, override val throwable: Throwable?) :
        Failure(errorMessage, throwable)
}
