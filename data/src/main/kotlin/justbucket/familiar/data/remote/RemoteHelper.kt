package justbucket.familiar.data.remote

import justbucket.familiar.domain.exception.Failure
import justbucket.familiar.domain.functional.Either
import justbucket.familiar.domain.utils.Logger
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection

/**
 * @author JustBucket on 2019-07-24
 */
object RemoteHelper {

    fun loadFromRemote(connection: HttpURLConnection): Either<Failure.NetworkFailure, String> {
        connection.connect()
        return if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            readStream(connection.inputStream)
        } else {
            Either.Left(Failure.NetworkFailure)
        }
    }

    private fun readStream(inputStream: InputStream): Either<Failure.NetworkFailure, String> {
        inputStream.use {
            return try {
                Either.Right(BufferedReader(InputStreamReader(it)).readText())
            } catch (e: IOException) {
                Logger.e(RemoteHelper::class.java.simpleName, "could not parse answer", e)
                Either.Left(Failure.NetworkFailure)
            }
        }
    }
}