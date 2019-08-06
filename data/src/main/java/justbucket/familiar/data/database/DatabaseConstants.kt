package justbucket.familiar.data.database

/**
 * @author JustBucket on 2019-07-24
 */
object DatabaseConstants {

    const val MASTER_TABLE_NAME = "master"

    const val DETAIL_TABLE_NAME = "detail"

    const val ID = "id"

    const val EXTENSION_NAME = "extension"

    const val MODEL_TITLE = "title"

    const val MODEL_CONTENT = "json_string"

    const val GET_ALL_MASTER_ENTITIES_QUERY = "SELECT * FROM $MASTER_TABLE_NAME"

    const val DELETE_MASTER_ENTITY_BY_ID = "DELETE FROM $MASTER_TABLE_NAME WHERE $ID = :id"

    const val GET_DETAIL_ENTITY_BY_ID_QUERY = "SELECT * FROM $DETAIL_TABLE_NAME WHERE $ID = :id"

    const val DELETE_DETAIL_ENTITY_BY_ID = "DELETE FROM $DETAIL_TABLE_NAME WHERE $ID = :id"
}