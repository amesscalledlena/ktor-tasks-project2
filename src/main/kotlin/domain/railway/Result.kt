package com.example.domain.railway

sealed class Result<out T, out F: ResultFailure> {
    data class Success<out T>(val value: T) : Result<T, Nothing>()
    data class Failure<F : ResultFailure>(val failure: F) : Result<Nothing, F>()
    companion object {
        @JvmStatic
        fun <T> success(value: T) = Success(value)

        @JvmStatic
        fun <F : ResultFailure> failure(failures: F) = Failure(failures)

        // 2-tuple
        inline fun <A, B, A_F : ResultFailure, B_F : ResultFailure, RESULT, RESULT_FAILURE : ResultFailure> zip(
            a: Result<A, A_F>,
            b: Result<B, B_F>,
            failure: RESULT_FAILURE,
            mapSuccess: (A, B) -> RESULT
        ): Result<RESULT, RESULT_FAILURE> {

            // 1. Check if 'a' is a failure OR 'b' is a failure
            return if (a.isFailure || b.isFailure) {
                val baseFailure = failure
                a.failureOrNull?.also { baseFailure.addCause(it) }
                b.failureOrNull?.also { baseFailure.addCause(it) }
                failure(baseFailure)
            } else {
                success(mapSuccess(a.successOrException, b.successOrException))
            }
        }

        // 3-tuple
        inline fun <A, B, C, A_F : ResultFailure, B_F : ResultFailure, C_F : ResultFailure, RESULT, RESULT_FAILURE : ResultFailure> zip(
            a: Result<A, A_F>,
            b: Result<B, B_F>,
            c: Result<C, C_F>,
            failure: RESULT_FAILURE,
            mapSuccess: (A, B, C) -> RESULT
        ): Result<RESULT, RESULT_FAILURE> {
            return if (a.isFailure || b.isFailure || c.isFailure) {
                val baseFailure = failure
                a.failureOrNull?.also { baseFailure.addCause(it) }
                b.failureOrNull?.also { baseFailure.addCause(it) }
                c.failureOrNull?.also { baseFailure.addCause(it) }
                failure(baseFailure)
            } else {
                success(mapSuccess(a.successOrException, b.successOrException, c.successOrException))
            }
        }

        // 4-tuple
        inline fun <A, B, C, D, A_F : ResultFailure, B_F : ResultFailure, C_F : ResultFailure, D_F : ResultFailure, RESULT, RESULT_FAILURE : ResultFailure> zip(
            a: Result<A, A_F>,
            b: Result<B, B_F>,
            c: Result<C, C_F>,
            d: Result<D, D_F>,
            failure: RESULT_FAILURE,
            mapSuccess: (A, B, C, D) -> RESULT
        ): Result<RESULT, RESULT_FAILURE> {
            return if (a.isFailure || b.isFailure || c.isFailure || d.isFailure) {
                val baseFailure = failure
                a.failureOrNull?.also { baseFailure.addCause(it) }
                b.failureOrNull?.also { baseFailure.addCause(it) }
                c.failureOrNull?.also { baseFailure.addCause(it) }
                d.failureOrNull?.also { baseFailure.addCause(it) }
                failure(baseFailure)
            } else {
                success(
                    mapSuccess(
                        a.successOrException,
                        b.successOrException,
                        c.successOrException,
                        d.successOrException
                    )
                )
            }
        }

    }

    val isFailure: Boolean
        get() = this is Failure

    val isSuccess: Boolean
        get() = this is Success

    val failureOrNull: F?
        get() = if (this is Failure) failure else null
    val successOrNull: T?
        get() = if (this is Success) value else null

    val failureOrException: F
        get() = if (this is Result.Failure) failure else TODO()
    val successOrException: T
        get() = if (this is Result.Success) value else TODO()

    inline fun onSuccess(action: (value: T) -> Unit): Result<T, F> {
        if (this is Result.Success) {
            action(value)
        }
        return this
    }

    inline fun onFailure(action: (value: F) -> Unit): Result<T, F> {
        if (this is Result.Failure) {
            action(this.failure)
        }
        return this

    }
}