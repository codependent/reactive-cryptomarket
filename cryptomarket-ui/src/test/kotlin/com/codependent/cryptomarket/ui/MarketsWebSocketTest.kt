package com.codependent.cryptomarket.ui

import org.junit.Test
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Mono
import java.net.URI
import java.util.concurrent.CountDownLatch


class MarketsWebSocketTest {

    @Test
    fun shouldInvokeWebSocket() {
        val latch = CountDownLatch(100)
        val output = EmitterProcessor.create<String>()
        val client = ReactorNettyWebSocketClient()

        val input = Mono.just("BTC").doOnNext { println(it) }

        val sessionMono = client.execute(URI.create("ws://localhost:8080/markets")) { session ->
            session.send(input.map {
                session.textMessage(it)
            }).thenMany(session.receive().map { it.payloadAsText }.subscribeWith(output).then()).then()
        }.doOnNext { c -> println(c) }.doOnTerminate { latch.countDown() }

        output.doOnSubscribe { s -> sessionMono.subscribe() }
                .doOnNext { c ->
                    println(c)
                    latch.countDown()
                }.subscribe()

        latch.await()
    }
}