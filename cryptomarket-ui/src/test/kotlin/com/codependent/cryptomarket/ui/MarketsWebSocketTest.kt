package com.codependent.cryptomarket.ui

import org.junit.Test
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Flux
import java.net.URI
import java.util.concurrent.CountDownLatch


class MarketsWebSocketTest {

    @Test
    fun shouldInvokeWebSocket() {
        val latch = CountDownLatch(2)
        val output = EmitterProcessor.create<String>()
        val client = ReactorNettyWebSocketClient()

        val input = Flux.just("hola", "adios").doOnNext {println(it) }

        val sessionMono = client.execute(URI.create("ws://localhost:8080/markets")) { session ->
            session.send(input.map { session.textMessage(it) })
                    .thenMany(session.receive().map{ it.payloadAsText}.subscribeWith(output).then()).then()
        }.doOnNext { c -> println(c) }.doOnTerminate { latch.countDown() }

        output.doOnSubscribe { s -> sessionMono.subscribe() }
                .doOnNext { c ->
                    println(c)
                    latch.countDown()
                }.subscribe()

        latch.await()
    }
}