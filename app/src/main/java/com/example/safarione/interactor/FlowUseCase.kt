package com.example.safarione.interactor

import kotlinx.coroutines.flow.Flow

fun interface FlowUseCase<out RESULT, in PARAMS> {

    fun create(
        params: PARAMS
    ): Flow<RESULT>
}