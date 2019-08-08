package justbucket.familiar.data

import android.database.sqlite.SQLiteException
import android.util.MalformedJsonException
import com.google.gson.Gson
import justbucket.familiar.content.extension.model.SearchModel
import justbucket.familiar.data.database.ContentDatabase
import justbucket.familiar.data.database.entity.DetailEntity
import justbucket.familiar.data.database.entity.MasterEntity
import justbucket.familiar.data.remote.RemoteHelper
import justbucket.familiar.domain.exception.Failure
import justbucket.familiar.domain.extension.ExtensionManager
import justbucket.familiar.domain.functional.Either
import justbucket.familiar.domain.functional.collect
import justbucket.familiar.domain.functional.flatMap
import justbucket.familiar.domain.repository.SearchRepository
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author JustBucket on 2019-07-24
 */
class SearchRepositoryImpl(
    private val extensionManager: ExtensionManager,
    database: ContentDatabase
) : SearchRepository {

    private val masterDao = database.getMasterDao()
    private val detailDao = database.getDetailDao()

    override suspend fun searchByQuery(query: String): Either<Failure.NetworkFailure, Set<SearchModel>> {
        val result: Either<Failure.NetworkFailure, MutableSet<SearchModel>> = Either.Right(mutableSetOf())
        try {
            for (extensionHolder in extensionManager.getExtensions()) {
                val uri = extensionHolder.value.locator.getSearchModelsByQuery(query)
                val either = RemoteHelper.loadFromRemote(URL(uri.toString()).openConnection() as HttpURLConnection)
                result.collect(either.flatMap { json ->
                    Either.Right(extensionHolder.value.creator.createSearchModel(json))
                }) { elements: Collection<SearchModel> -> this.toMutableSet().addAll(elements) }
            }
        } catch (e: MalformedJsonException) {
            result.collect(Either.Left(Failure.NetworkFailure))
        }
        return result
    }

    override suspend fun addContentEntry(searchModel: SearchModel): Either<Failure, Long> {
        val extension = extensionManager.getExtensions()[searchModel.extensionName]
        return try {
            val masterEither = RemoteHelper.loadFromRemote(
                URL(extension.locator.getMasterForSearch(searchModel).toString()).openConnection() as HttpURLConnection
            ).flatMap {
                val masterModel =
                    extension.creator.createMasterModel(it)
                val id = masterDao.insertMasterEntity(
                    MasterEntity(
                        null, extension.creator.extensionName,
                        masterModel.title, Gson().toJson(masterModel)
                    )
                )
                Either.Right(id)
            }
            if (masterEither is Either.Right) {
                RemoteHelper.loadFromRemote(
                    URL(
                        extension.locator.getDetailsForSearch(searchModel).toString()
                    ).openConnection() as HttpURLConnection
                ).flatMap {
                    val detailModel =
                        extension.creator.createDetailModel(it)
                    val id = detailDao.insertDetailEntity(
                        DetailEntity(
                            masterEither.b, extension.creator.extensionName,
                            detailModel.title, Gson().toJson(detailModel)
                        )
                    )
                    Either.Right(id)
                }

            } else masterEither
        } catch (e: SQLiteException) {
            Either.Left(Failure.DBFailure)
        }
    }
}