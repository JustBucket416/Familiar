package justbucket.familiar.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This abstraction represents an execution unit for different use cases (this means than any use
 * case in the application should implement this contract).
 *
 * By convention each [UseCase] implementation will execute its job in a background thread
 * (kotlin coroutine) and will post the result in the UI thread.
 */

abstract class UseCase<out Type, in Params>(
    private val coroutineContext: CoroutineContext
) where Type : Any {

    protected val ILLEGAL_EXCEPTION_MESSAGE = "Params can't be null!"
    private var currentJob: Job? = null
    private var postJob: Job? = null

    protected abstract suspend fun run(params: Params? = null): Type

    fun execute(
        scope: CoroutineScope,
        onResult: ((Type) -> Unit)? = null,
        params: Params? = null,
        cancelLastRequest: Boolean = true
    ) {
        if (cancelLastRequest) {
            postJob?.cancel()
            currentJob?.cancel()
        }
        val job = scope.async(context = Dispatchers.IO) { run(params) }
        postJob = scope.launch(context = coroutineContext) { onResult?.invoke(job.await()) }
        currentJob = job
    }
}