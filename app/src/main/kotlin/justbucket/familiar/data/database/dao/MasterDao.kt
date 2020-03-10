package justbucket.familiar.data.database.dao

import androidx.room.*
import justbucket.familiar.data.database.DELETE_MASTER_ENTITY_BY_ID
import justbucket.familiar.data.database.FIND_EXISTING
import justbucket.familiar.data.database.GET_ALL_MASTER_ENTITIES_QUERY
import justbucket.familiar.data.database.entity.MasterEntity

/**
 * @author JustBucket on 2019-07-24
 */
@Dao
abstract class MasterDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insertMasterEntity(entity: MasterEntity): Long

    @Update
    abstract fun updateMasterEntity(entity: MasterEntity)

    @Query(GET_ALL_MASTER_ENTITIES_QUERY)
    abstract fun getAllMasterEntities(): List<MasterEntity>

    @Query(DELETE_MASTER_ENTITY_BY_ID)
    abstract fun deleteMasterEntityById(id: Long)

    @Query(FIND_EXISTING)
    protected abstract fun findExisting(extensionName: String, modelName: String): MasterEntity?

    @Transaction
    open fun insertOrUpdate(entity: MasterEntity): Long {
        val e = findExisting(entity.extensionName, entity.modelName)
        return if (e != null) {
            updateMasterEntity(e)
            e.id!!
        } else {
            insertMasterEntity(entity)
        }
    }
}