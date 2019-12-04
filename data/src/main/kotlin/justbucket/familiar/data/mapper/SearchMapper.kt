package justbucket.familiar.data.mapper

import justbucket.familiar.content.extension.ExtensionModelCreator
import justbucket.familiar.content.extension.model.SearchModel
import justbucket.familiar.data.database.entity.MasterEntity

/**
 * @author JustBucket on 2019-07-24
 */
class SearchMapper : Mapper<Set<SearchModel>, MasterEntity> {

    override fun mapToDomain(creator: ExtensionModelCreator, data: MasterEntity): Set<SearchModel> {
        return creator.createSearchModel(data.modelContent)
    }

    override fun mapToData(creator: ExtensionModelCreator, domain: Set<SearchModel>): MasterEntity {
        throw IllegalStateException()
    }
}