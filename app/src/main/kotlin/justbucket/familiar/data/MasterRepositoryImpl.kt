package justbucket.familiar.data

import android.database.sqlite.SQLiteException
import com.google.gson.stream.MalformedJsonException
import justbucket.familiar.data.database.ContentDatabase
import justbucket.familiar.domain.extension.ExtensionManager
import justbucket.familiar.domain.repository.MasterRepository
import justbucket.familiar.domain.utils.logE
import justbucket.familiar.extension.exception.Failure.DBFailure
import justbucket.familiar.extension.functional.Either
import justbucket.familiar.extension.model.MasterModel

/**
 * @author JustBucket on 2019-07-23
 */
class MasterRepositoryImpl(
    private val extensionManager: ExtensionManager,
    contentDatabase: ContentDatabase
) : MasterRepository {

    private val dao = contentDatabase.getMasterDao()

    override suspend fun loadAllModels(): Set<MasterModel> {
        val result = mutableSetOf<MasterModel>()
        try {
            dao.getAllMasterEntities().forEach {
                val creator = extensionManager.getExtensions()[it.extensionName].creator
                result.add(creator.createMasterModel(it.modelContent))
            }
        } catch (e: Exception) {
            when (e) {
                is MalformedJsonException, is SQLiteException -> {
                    logE("Failed to load all models", e.localizedMessage, e)
                }
                else -> throw (e)
            }
        }
        return result
    }

    override suspend fun deleteModel(modelId: Long) =
        try {
            dao.deleteMasterEntityById(modelId)
            Either.Right(modelId)
        } catch (e: SQLiteException) {
            Either.Left(DBFailure("Failed to delete model", e))
        }

}