package com.example.safarione.utils

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class EventChannel<T>(
    buffer: Int = Channel.CONFLATED,
    strategy: BufferOverflow = BufferOverflow.DROP_OLDEST
) {

    private val channel = Channel<T>(capacity = buffer, onBufferOverflow = strategy)

    val flow: Flow<T>
        get() = channel.receiveAsFlow()

    suspend fun send(value: T) = channel.send(value)
}

fun <T> eventChannelOf(buffer: Int = 1, strategy: BufferOverflow = BufferOverflow.DROP_OLDEST) =
    EventChannel<T>(buffer = buffer, strategy = strategy)