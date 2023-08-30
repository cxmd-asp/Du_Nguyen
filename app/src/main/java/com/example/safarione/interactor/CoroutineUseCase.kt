package com.example.safarione.interactor

import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

fun interface AbstractCoroutineUseCase<out RESULT, in PARAMS> {

    suspend fun fetch(
        context: CoroutineContext,
        params: PARAMS,
    ): RESULT
}

abstract class CoroutineUseCase<out RESULT, in PARAMS> : AbstractCoroutineUseCase<RESULT, PARAMS> {

    override suspend fun fetch(
        context: CoroutineContext,
        params: PARAMS,
    ): RESULT = withContext(context) {
        run(params = params)
    }

    abstract suspend fun run(params: PARAMS): RESULT
}