package justbucket.familiar.data.remote

import justbucket.familiar.domain.utils.logE
import justbucket.familiar.extension.exception.Failure.NetworkFailure
import justbucket.familiar.extension.functional.Either
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection

/**
 * @author JustBucket on 2019-07-24
 */
object RemoteHelper {

    fun loadFromRemote(connection: HttpURLConnection): Either<NetworkFailure, String> {
        connection.connect()
        return if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            readStream(connection.inputStream)
        } else {
            Either.Left(NetworkFailure("Failed to load from remote", IOException()))
        }
    }

    private fun readStream(inputStream: InputStream): Either<NetworkFailure, String> {
        inputStream.use {
            return try {
                Either.Right(BufferedReader(InputStreamReader(it)).readText())
            } catch (e: IOException) {
                logE(RemoteHelper::class.java.simpleName, "could not parse answer", e)
                Either.Left(NetworkFailure("Failed to read stream", e))
            }
        }
    }
}