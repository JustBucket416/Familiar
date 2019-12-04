package justbucket.familiar.data

import com.google.gson.stream.MalformedJsonException
import justbucket.familiar.content.extension.exception.Failure
import justbucket.familiar.content.extension.functional.Either
import justbucket.familiar.data.database.ContentDatabase
import justbucket.familiar.domain.extension.ExtensionManager
import justbucket.familiar.domain.repository.DetailRepository

/**
 * @author JustBucket on 2019-07-24
 */
class DetailRepositoryImpl(
        private val extensionManager: ExtensionManager,
        contentDatabase: ContentDatabase
) : DetailRepository {

    private val dao = contentDatabase.getDetailDao()

    override suspend fun loadModelDetails(modelId: Long) = try {
        val entity = dao.getDetailEntityById(modelId)
        val creator = extensionManager.getExtensions()[entity.extensionName].creator
        Either.Right(creator.createDetailModel(entity.modelContent))
    } catch (e: MalformedJsonException) {
        Either.Left(Failure.DBFailure)
    }
}