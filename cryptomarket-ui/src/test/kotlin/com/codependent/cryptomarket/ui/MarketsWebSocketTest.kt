package com.codependent.cryptomarket.ui

import org.junit.Test
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Mono
import java.net.URI
import java.util.concurrent.CountDownLatch


class MarketsWebSocketTest {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Test
    fun shouldInvokeWebSocket() {
        val latch = CountDownLatch(20)
        val output = EmitterProcessor.create<String>()
        val client = ReactorNettyWebSocketClient()

        val input = Mono.just("BTC").doOnNext { println(it) }

        val sessionMono = client.execute(URI.create("ws://localhost:8080/markets")) { session ->
            session.send(input.map {
                session.textMessage(it)
            }).thenMany(session.receive()
                    .map { it.payloadAsText }
                    .subscribeWith(output).then()).then()
        }

        output.doOnSubscribe { _ -> sessionMono.subscribe() }
            .doOnNext { c ->
                logger.info("Received [{}]", c)
                latch.countDown()
            }.subscribe()

        latch.await()
    }
}