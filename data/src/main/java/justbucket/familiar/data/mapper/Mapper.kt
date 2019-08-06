package justbucket.familiar.data.mapper

import justbucket.familiar.content.extension.ExtensionModelCreator

/**
 * @author JustBucket on 2019-07-24
 */
interface Mapper<Domain, Data> {

    fun mapToDomain(creator: ExtensionModelCreator, data: Data): Domain

    fun mapToData(creator: ExtensionModelCreator, domain: Domain): Data
}