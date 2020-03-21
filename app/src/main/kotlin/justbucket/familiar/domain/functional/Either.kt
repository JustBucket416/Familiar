package justbucket.familiar.domain.functional

/**
 * Represents a value of one of two possible types (a disjoint union).
 * Instances of [Either] are either an instance of [Left] or [Right].
 * FP Convention dictates that [Left] is used for "failure"
 * and [Right] is used for "success".
 *
 * @see Left
 * @see Right
 */
sealed class Either<out L, out R> {
    /** * Represents the left side of [Either] class which by convention is a "Failure". */
    data class Left<out L>(val a: L) : Either<L, Nothing>()

    /** * Represents the right side of [Either] class which by convention is a "Success". */
    data class Right<out R>(val b: R) : Either<Nothing, R>()

    val isRight get() = this is Right<R>
    val isLeft get() = this is Left<L>

    fun <L> left(a: L) = Left(a)
    suspend fun <R> right(b: R) = Right(b)

    fun either(fnL: (L) -> Any, fnR: (R) -> Any): Any =
        when (this) {
            is Left -> fnL(a)
            is Right -> fnR(b)
        }
}

// Credits to Alex Hart -> https://proandroiddev.com/kotlins-nothing-type-946de7d464fb
// Composes 2 functions
suspend fun <A, B, C> (suspend (A) -> B).c(f: suspend (B) -> C): suspend (A) -> C = {
    f(this(it))
}

suspend fun <T, L, R> Either<L, R>.flatMap(fn: suspend (R) -> Either<L, T>): Either<L, T> =
    when (this) {
        is Either.Left -> this
        is Either.Right -> fn(b)
    }

suspend fun <T, L, R> Either<L, R>.map(fn: suspend (R) -> (T)): Either<L, T> = this.flatMap(fn.c(::right))