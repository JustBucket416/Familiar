package justbucket.familiar.data

import justbucket.familiar.domain.extension.ExtensionHolder
import justbucket.familiar.domain.repository.SearchRepository
import justbucket.familiar.extension.model.MasterModel
import justbucket.familiar.utils.logE
import justbucket.familiar.utils.logI
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

/**
 * @author JustBucket on 08-Feb-2020
 */
class SearchRepositoryImpl(private val extensions: List<ExtensionHolder>) : SearchRepository {

    override suspend fun searchByQuery(query: String): List<MasterModel> =
        withContext(coroutineContext) {
            val models = mutableListOf<MasterModel>()
            logI(message = "searching for $query")
            extensions.map {
                launch {
                    if (it.locator.extensionName.contains("youtube", ignoreCase = true)) {
                        runCatching {
                            it.locator.getMasterForSearch(query)
                        }.getOrElse { throwable ->
                            logE(
                                message = throwable.localizedMessage,
                                cause = throwable
                            )
                            emptySet()
                        }.let { models.addAll(it) }
                    }
                }
            }.joinAll()
            models
        }
}