package justbucket.familiar.domain.exception

import justbucket.familiar.content.extension.exception.Failure

/**
 * @author JustBucket on 2019-08-31
 */
class DBFailure(override val errorMessage: String): Failure(errorMessage)
