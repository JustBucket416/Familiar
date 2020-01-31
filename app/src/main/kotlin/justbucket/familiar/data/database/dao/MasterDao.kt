package justbucket.familiar.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import justbucket.familiar.data.database.DatabaseConstants
import justbucket.familiar.data.database.entity.MasterEntity

/**
 * @author JustBucket on 2019-07-24
 */
@Dao
interface MasterDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertMasterEntity(entity: MasterEntity): Long

    @Update
    fun updateMasterEntity(entity: MasterEntity)

    @Query(DatabaseConstants.GET_ALL_MASTER_ENTITIES_QUERY)
    fun getAllMasterEntities(): List<MasterEntity>

    @Query(DatabaseConstants.DELETE_MASTER_ENTITY_BY_ID)
    fun deleteMasterEntityById(id: Long)
}