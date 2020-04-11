package justbucket.familiar.data

import android.database.sqlite.SQLiteException
import androidx.room.InvalidationTracker
import justbucket.familiar.data.database.ContentDatabase
import justbucket.familiar.data.database.MASTER_TABLE_NAME
import justbucket.familiar.data.database.entity.MasterEntity
import justbucket.familiar.domain.extension.ExtensionManager
import justbucket.familiar.domain.functional.flatMap
import justbucket.familiar.domain.repository.DetailRepository
import justbucket.familiar.domain.repository.MasterRepository
import justbucket.familiar.extension.model.MasterModel
import justbucket.familiar.utils.logE
import justbucket.familiar.utils.logI
import justbucket.familiar.utils.logW
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * @author JustBucket on 2019-07-23
 */
@FlowPreview
@ExperimentalCoroutinesApi
class MasterRepositoryImpl(
    private val extensionManager: ExtensionManager,
    private val contentDatabase: ContentDatabase,
    private val detailRepository: DetailRepository
) : MasterRepository, InvalidationTracker.Observer(MASTER_TABLE_NAME) {

    private val dao = contentDatabase.getMasterDao()
    private val channel = ConflatedBroadcastChannel<List<MasterModel>>()
    private val currentList = mutableListOf<MasterModel>()
    private val mutex = Mutex()
    private var searchJob: Job? = null

    init {
        contentDatabase.invalidationTracker.addObserver(this)
        loadModelsFromDb()
    }

    override fun onInvalidated(tables: MutableSet<String>) {
        if (contentDatabase.inTransaction().not()) {
            loadModelsFromDb()
        }
    }

    override fun loadAllModels(): Flow<List<MasterModel>> = channel.asFlow()

    override fun searchByQuery(scope: CoroutineScope, query: String) {
        currentList.clear()
        searchJob?.cancel()
        searchJob = scope.launch(Dispatchers.IO) {
            loadModelsFromDb(query, this)
            extensionManager.getExtensions().values
                .map { it.locator }
                .forEach { locator ->
                    launch(Dispatchers.IO) {
                        modifyCurrentListAndSend {
                            addAll(locator.getMasterForSearch(query))
                        }
                    }
                }
        }
    }

    override suspend fun deleteModel(modelId: Long) {
        dao.deleteMasterEntityById(modelId)
    }

    override suspend fun saveModel(masterModel: MasterModel) {
        if (!extensionManager.getExtensions().containsKey(masterModel.extensionName)) {
            return
        }
        val mapper = extensionManager.getExtensions()[masterModel.extensionName].mapper
        mapper.mapMasterToLocal(masterModel).orEmpty()
            .let { modelContent ->
                MasterEntity(null, masterModel.extensionName, masterModel.title, modelContent)
            }
            .let { entity ->
                dao.insertOrUpdate(entity)
            }
        detailRepository.loadModelDetails(masterModel)
            .flatMap { detailModel -> detailRepository.saveDetailModel(detailModel) }
    }

    private fun loadModelsFromDb(query: String? = null, scope: CoroutineScope = GlobalScope) =
        scope.launch(Dispatchers.IO) {
            try {
                val groupedEntities = dao.getAllMasterEntities()
                    .let { entities ->
                        if (query != null) {
                            entities.filter { it.modelName.contains(query, ignoreCase = true) }
                        } else {
                            entities
                        }
                    }.groupBy {
                        it.extensionName
                    }
                if (groupedEntities.isNotEmpty()) {
                    groupedEntities.forEach { (extensionName, modelList) ->
                        mapModels(extensionName, modelList)
                    }
                } else {
                    modifyCurrentListAndSend {
                        mergeListFromDb(emptyList())
                    }
                }
            } catch (e: SQLiteException) {
                val message = "Failed to load all models"
                logE(message = message, cause = e)
            }
        }

    private suspend fun mapModels(
        extensionName: String,
        modelList: List<MasterEntity>
    ) {
        if (extensionManager.getExtensions().containsKey(extensionName)) {
            modifyCurrentListAndSend {
                modelList.map { entity ->
                    extensionManager.getExtensions()[extensionName].mapper.mapLocalToMaster(
                        entity.id!!,
                        entity.modelContent
                    ) ?: MasterModel(
                        id = entity.id,
                        extensionName = entity.extensionName,
                        title = entity.modelName
                    )
                }.let { mergeListFromDb(it) }
            }
        } else {
            logW(message = "Could not find extension with name $extensionName")
        }
    }

    private fun mergeListFromDb(
        listFromDb: List<MasterModel>
    ) {
        val modelsNotInDb = currentList.filter { it.detailViewLink != null }
        currentList.clear()
        currentList.addAll(modelsNotInDb)
        currentList.addAll(listFromDb)
    }

    private suspend inline fun modifyCurrentListAndSend(crossinline func: suspend MutableList<MasterModel>.() -> Any) {
        mutex.withLock {
            currentList.func()
            logI(message = "sending models $currentList")
            channel.send(currentList)
        }
    }

}