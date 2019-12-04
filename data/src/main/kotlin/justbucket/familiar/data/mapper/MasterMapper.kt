package justbucket.familiar.data.mapper

import com.google.gson.Gson
import justbucket.familiar.content.extension.ExtensionModelCreator
import justbucket.familiar.content.extension.model.MasterModel
import justbucket.familiar.data.database.entity.MasterEntity

/**
 * @author JustBucket on 2019-07-24
 */
class MasterMapper : Mapper<MasterModel, MasterEntity> {

    override fun mapToDomain(creator: ExtensionModelCreator, data: MasterEntity) =
        creator.createMasterModel(data.modelContent)

    override fun mapToData(creator: ExtensionModelCreator, domain: MasterModel) =
        MasterEntity(null, creator.extensionName, domain.title, Gson().toJson(domain))

}