package justbucket.familiar.extension

import justbucket.familiar.extension.exception.Failure
import justbucket.familiar.extension.exception.Failure.FeatureFailure
import justbucket.familiar.extension.functional.Either
import justbucket.familiar.extension.model.DetailModel
import justbucket.familiar.extension.model.MasterModel
import justbucket.familiar.extension.model.ShareModel

/**
 * @author JustBucket on 2019-07-24
 */
interface ExtensionLocator {

    val extensionName: String

    fun getMasterForSearch(query: String): Either<Failure, Set<MasterModel>>

    fun getDetailsForSearch(masterModel: MasterModel): Either<FeatureFailure, DetailModel>

    fun getMasterForShare(shareModel: ShareModel): Either<FeatureFailure, MasterModel>

    fun getDetailsForShare(shareModel: ShareModel): Either<FeatureFailure, DetailModel>
}