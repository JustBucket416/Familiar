package justbucket.familiar.data

import android.database.sqlite.SQLiteException
import android.util.MalformedJsonException
import justbucket.familiar.data.database.ContentDatabase
import justbucket.familiar.data.database.entity.DetailEntity
import justbucket.familiar.domain.exception.Failure
import justbucket.familiar.domain.extension.ExtensionManager
import justbucket.familiar.domain.functional.Either
import justbucket.familiar.domain.repository.DetailRepository
import justbucket.familiar.domain.utils.logE
import justbucket.familiar.extension.model.DetailModel
import justbucket.familiar.extension.model.MasterModel
import java.io.IOException

/**
 * @author JustBucket on 2019-07-24
 */
class DetailRepositoryImpl(
    private val extensionManager: ExtensionManager,
    contentDatabase: ContentDatabase
) : DetailRepository {

    private val dao = contentDatabase.getDetailDao()

    override suspend fun loadModelDetails(masterModel: MasterModel) = try {
        val model = if (masterModel.detailViewLink?.isNotEmpty() == true) {
            loadFromLocator(masterModel)
        } else {
            loadFromDb(masterModel.id)
        }
        Either.Right(model)
    } catch (e: Exception) {
        when (e) {
            is MalformedJsonException -> Either.Left(Failure.FeatureFailure(e.localizedMessage, e))
            is SQLiteException -> Either.Left(Failure.DBFailure(e.localizedMessage, e))
            is IOException -> Either.Left(Failure.NetworkFailure(e.localizedMessage, e))
            else -> throw e
        }

    }

    override suspend fun saveDetailModel(detailModel: DetailModel): Either<Failure.DBFailure, Long> {
        return try {
            val mapper = extensionManager.getExtensions()[detailModel.extensionName].mapper
            return mapper.mapDetailToLocal(detailModel).orEmpty()
                .let { modelContent ->
                    DetailEntity(null, detailModel.extensionName, detailModel.title, modelContent)
                }
                .let { entity ->
                    dao.insertDetailEntity(entity)
                }
                .let { id ->
                    Either.Right(id)
                }
        } catch (e: SQLiteException) {
            logE(message = e.localizedMessage, cause = e)
            Either.Left(Failure.DBFailure(e.localizedMessage, e))
        }
    }

    private fun loadFromDb(modelId: Long): DetailModel {
        val entity = dao.getDetailEntityById(modelId)
        val creator = extensionManager.getExtensions()[entity.extensionName].mapper
        return creator.mapLocalToDetail(entity.modelContent)
            ?: DetailModel(title = entity.modelName)
    }

    private suspend fun loadFromLocator(masterModel: MasterModel): DetailModel {
        return extensionManager.getExtensions()[masterModel.extensionName].locator.getDetailsForSearch(masterModel)
    }
}
