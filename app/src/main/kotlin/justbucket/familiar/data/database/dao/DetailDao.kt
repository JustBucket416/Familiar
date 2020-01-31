package justbucket.familiar.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import justbucket.familiar.data.database.DatabaseConstants
import justbucket.familiar.data.database.entity.DetailEntity

/**
 * @author JustBucket on 2019-07-24
 */
@Dao
interface DetailDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertDetailEntity(entity: DetailEntity): Long

    @Delete
    fun deleteDetailEntity(entity: DetailEntity)

    @Update
    fun updateDetailEntity(entity: DetailEntity)

    @Query(DatabaseConstants.GET_DETAIL_ENTITY_BY_ID_QUERY)
    fun getDetailEntityById(id: Long): DetailEntity
}