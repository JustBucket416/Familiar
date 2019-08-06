package justbucket.familiar.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import justbucket.familiar.data.database.dao.DetailDao
import justbucket.familiar.data.database.dao.MasterDao
import justbucket.familiar.data.database.entity.DetailEntity
import justbucket.familiar.data.database.entity.MasterEntity
import justbucket.familiar.domain.utils.SingletonHolder

/**
 * @author JustBucket on 2019-07-24
 */
@Database(entities = [MasterEntity::class, DetailEntity::class], version = 1, exportSchema = false)
abstract class ContentDatabase : RoomDatabase() {

    abstract fun getMasterDao(): MasterDao

    abstract fun getDetailDao(): DetailDao

    companion object : SingletonHolder<ContentDatabase, Context>({
        Room.databaseBuilder(it, ContentDatabase::class.java, "database.db").build()
    })

}