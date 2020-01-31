package justbucket.familiar.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import justbucket.familiar.data.database.DatabaseConstants

/**
 * @author JustBucket on 2019-07-24
 */
@Entity(tableName = DatabaseConstants.MASTER_TABLE_NAME)
data class MasterEntity(
    @PrimaryKey
    @ColumnInfo(name = DatabaseConstants.ID)
    val id: Long? = null,
    @ColumnInfo(name = DatabaseConstants.EXTENSION_NAME)
    val extensionName: String,
    @ColumnInfo(name = DatabaseConstants.MODEL_TITLE)
    val modelName: String,
    @ColumnInfo(name = DatabaseConstants.MODEL_CONTENT)
    val modelContent: String
)