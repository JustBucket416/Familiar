package justbucket.familiar.data

import justbucket.familiar.domain.extension.ExtensionHolder
import justbucket.familiar.domain.repository.SearchRepository
import justbucket.familiar.domain.utils.logE
import justbucket.familiar.extension.model.MasterModel
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

/**
 * @author JustBucket on 08-Feb-2020
 */
class SearchRepositoryImpl(private val extensions: List<ExtensionHolder>) : SearchRepository {

    override suspend fun searchByQuery(query: String): Set<MasterModel> =
        withContext(coroutineContext) {
            val models = mutableSetOf<MasterModel>()
            extensions.map {
                launch {
                    runCatching {
                        it.locator.getMasterForSearch(query)
                    }.getOrElse { throwable ->
                        logE(message = throwable.localizedMessage, cause = throwable)
                        emptySet()
                    }.let { models.addAll(it) }
                }
            }.joinAll()
            models
        }
}