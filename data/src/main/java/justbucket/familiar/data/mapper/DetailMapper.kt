package justbucket.familiar.data.mapper

import com.google.gson.Gson
import justbucket.familiar.content.extension.ExtensionModelCreator
import justbucket.familiar.content.extension.model.DetailModel
import justbucket.familiar.data.database.entity.DetailEntity

/**
 * @author JustBucket on 2019-07-24
 */
class DetailMapper : Mapper<DetailModel, DetailEntity> {

    override fun mapToDomain(creator: ExtensionModelCreator, data: DetailEntity): DetailModel {
        return creator.createDetailModel(data.modelContent)
    }

    override fun mapToData(creator: ExtensionModelCreator, domain: DetailModel): DetailEntity {
        return DetailEntity(null, creator.extensionName, domain.title, Gson().toJson(domain))
    }
}