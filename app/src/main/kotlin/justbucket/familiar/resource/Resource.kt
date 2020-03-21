package justbucket.familiar.resource

/**
 * @author JustBucket on 2019-07-15
 */
sealed class Resource<out T> {

    class Loading<out T>: Resource<T>()

    data class Success<out T>(val data: T): Resource<T>()

    data class Error<out T>(val errorMessage: String): Resource<T>()
}