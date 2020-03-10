package justbucket.familiar.data.database.dao

import androidx.room.*
import justbucket.familiar.data.database.GET_DETAIL_ENTITY_BY_ID_QUERY
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

    @Query(GET_DETAIL_ENTITY_BY_ID_QUERY)
    fun getDetailEntityById(id: Long): DetailEntity
}