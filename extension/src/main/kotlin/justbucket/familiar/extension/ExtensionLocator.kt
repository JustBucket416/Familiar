package justbucket.familiar.extension

import justbucket.familiar.extension.exception.Failure.FeatureFailure
import justbucket.familiar.extension.functional.Either
import justbucket.familiar.extension.model.DetailModel
import justbucket.familiar.extension.model.MasterModel
import justbucket.familiar.extension.model.SearchModel
import justbucket.familiar.extension.model.ShareModel

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