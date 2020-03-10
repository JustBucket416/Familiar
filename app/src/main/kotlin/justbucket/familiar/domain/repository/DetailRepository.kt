package justbucket.familiar.domain.repository

import justbucket.familiar.domain.exception.Failure
import justbucket.familiar.domain.functional.Either
import justbucket.familiar.extension.model.DetailModel
import justbucket.familiar.extension.model.MasterModel

/**
 * @author JustBucket on 2019-07-12
 */
interface DetailRepository {

    suspend fun loadModelDetails(masterModel: MasterModel): Either<Failure, DetailModel>

    suspend fun saveDetailModel(detailModel: DetailModel): Either<Failure.DBFailure, Long>
}