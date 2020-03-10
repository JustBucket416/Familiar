package justbucket.familiar.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import justbucket.familiar.data.database.*

/**
 * @author JustBucket on 2019-07-24
 */
@Entity(tableName = MASTER_TABLE_NAME)
data class MasterEntity(
    @PrimaryKey
    @ColumnInfo(name = ID)
    val id: Long? = null,
    @ColumnInfo(name = EXTENSION_NAME)
    val extensionName: String,
    @ColumnInfo(name = MODEL_TITLE)
    val modelName: String,
    @ColumnInfo(name = MODEL_CONTENT)
    val modelContent: String
)