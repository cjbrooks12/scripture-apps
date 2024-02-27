package com.caseyjbrooks.api

public sealed class Either<out L, out R> {
    public data class Left<out L>(val value: L) : Either<L, Nothing>()
    public data class Right<out R>(val value: R) : Either<Nothing, R>()
}

public fun <L> L.asLeft(): Either.Left<L> = Either.Left(this)
public fun <R> R.asRight(): Either.Right<R> = Either.Right(this)

public inline fun <reified L, reified R> Either<L, R>.requireLeft(): L = if (this is Either.Left<L>) {
    this.value
} else {
    error("$this was not ${L::class.simpleName}")
}

public inline fun <reified L, reified R> Either<L, R>.requireRight(): R = if (this is Either.Right<R>) {
    this.value
} else {
    error("$this was not ${R::class.simpleName}")
}

public inline fun <L, R, U> Either<L, R>.mapLeft(block: (L) -> U): Either<U, R> = when (this) {
    is Either.Left<L> -> this.value.let(block).asLeft()
    is Either.Right<R> -> this.value.asRight()
}

public inline fun <L, R, U> Either<L, R>.mapRight(block: (R) -> U): Either<L, U> = when (this) {
    is Either.Left<L> -> this.value.asLeft()
    is Either.Right<R> -> this.value.let(block).asRight()
}

public inline fun <reified L, reified R> Either<L, R>.orElseGet(block: () -> L): L = when (this) {
    is Either.Left<L> -> this.value
    is Either.Right<R> -> block()
}

public inline fun <reified L, reified R> Either<L, R>.fold(
    onLeft: (L) -> Unit,
    onRight: (R) -> Unit,
): Either<L, R> = this.also {
    when (it) {
        is Either.Left<L> -> onLeft(it.value)
        is Either.Right<R> -> onRight(it.value)
    }
}
