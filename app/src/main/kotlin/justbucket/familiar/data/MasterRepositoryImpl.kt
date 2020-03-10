package justbucket.familiar.data

import android.database.sqlite.SQLiteException
import justbucket.familiar.data.database.ContentDatabase
import justbucket.familiar.data.database.entity.MasterEntity
import justbucket.familiar.domain.exception.Failure
import justbucket.familiar.domain.exception.Failure.DBFailure
import justbucket.familiar.domain.extension.ExtensionManager
import justbucket.familiar.domain.functional.Either
import justbucket.familiar.domain.repository.MasterRepository
import justbucket.familiar.domain.utils.logE
import justbucket.familiar.extension.model.MasterModel
import org.json.JSONException

/**
 * @author JustBucket on 2019-07-23
 */
class MasterRepositoryImpl(
    private val extensionManager: ExtensionManager,
    contentDatabase: ContentDatabase
) : MasterRepository {

    private val dao = contentDatabase.getMasterDao()

    override suspend fun loadAllModels(): Pair<Failure?, Set<MasterModel>> {
        val result = mutableSetOf<MasterModel>()
        var failure: Failure? = null
        try {
            dao.getAllMasterEntities().forEach {
                val mapper = extensionManager.getExtensions()[it.extensionName].mapper
                try {
                    val model = mapper.mapLocalToMaster(it.modelContent) ?: MasterModel(
                        id = it.id ?: -1L,
                        extensionName = it.extensionName,
                        title = it.modelName
                    )
                    result.add(model)
                } catch (e: JSONException) {
                    logE(message = e.localizedMessage, cause = e)
                    failure = Failure.FeatureFailure(e.localizedMessage, e)
                }
            }
        } catch (e: SQLiteException) {
            val message = "Failed to load all models"
            logE(message = message, cause = e)
            failure = DBFailure(message, e)
        }
        return Pair(failure, result)
    }

    override suspend fun deleteModel(modelId: Long): Either<DBFailure, Long> =
        try {
            dao.deleteMasterEntityById(modelId)
            Either.Right(modelId)
        } catch (e: SQLiteException) {
            val errorMessage = "Failed to delete model"
            logE(message = errorMessage, cause = e)
            Either.Left(DBFailure(errorMessage, e))
        }

    override suspend fun saveModel(masterModel: MasterModel): Either<DBFailure, Long> {
        return try {
            val mapper = extensionManager.getExtensions()[masterModel.extensionName].mapper
            return mapper.mapMasterToLocal(masterModel).orEmpty()
                .let { modelContent ->
                    MasterEntity(null, masterModel.extensionName, masterModel.title, modelContent)
                }
                .let { entity ->
                    dao.insertOrUpdate(entity)
                }
                .let { id ->
                    Either.Right(id)
                }
        } catch (e: SQLiteException) {
            logE(message = e.localizedMessage, cause = e)
            Either.Left(DBFailure(e.localizedMessage, e))
        }
    }
}