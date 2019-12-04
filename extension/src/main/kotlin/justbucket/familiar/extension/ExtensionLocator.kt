package justbucket.familiar.content.extension

import justbucket.familiar.content.extension.exception.Failure.FeatureFailure
import justbucket.familiar.content.extension.functional.Either
import justbucket.familiar.content.extension.model.DetailModel
import justbucket.familiar.content.extension.model.MasterModel
import justbucket.familiar.content.extension.model.SearchModel
import justbucket.familiar.content.extension.model.ShareModel

/**
 * @author JustBucket on 2019-07-24
 */
interface ExtensionLocator {

    val extensionName: String

    fun getSearchModelsByQuery(query: String): Either<FeatureFailure, List<SearchModel>>

    fun getMasterForShare(shareModel: ShareModel): Either<FeatureFailure, MasterModel>

    fun getDetailsForShare(shareModel: ShareModel): Either<FeatureFailure, DetailModel>

    fun getMasterForSearch(searchModel: SearchModel): Either<FeatureFailure, MasterModel>

    fun getDetailsForSearch(searchModel: SearchModel): Either<FeatureFailure, DetailModel>
}