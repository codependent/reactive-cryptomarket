package com.codependent.cryptomarket.ui

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import java.net.URI
import java.time.Duration
import java.util.concurrent.CountDownLatch


class MarketsWebSocketTest {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Test
    fun shouldInvokeWebSocket() {
        val latch = CountDownLatch(20)

        val sink = Sinks.many().multicast().onBackpressureBuffer<String>()
        val client = ReactorNettyWebSocketClient()
        val input = Mono.just("BTC").doOnNext { logger.info("on next {}", it) }

        val sessionMono = client.execute(URI.create("ws://localhost:8080/markets")) { session ->
            session.send(input.map {
                session.textMessage(it)
            }).thenMany(session.receive()
                .map { it.payloadAsText })
                .doOnNext { sink.tryEmitNext(it) }
                .then()
        }

        val sinkFlux = sink.asFlux()
        sinkFlux
            .doOnNext { c ->
                logger.info("Received [{}]", c)
                latch.countDown()
            }
            .doOnSubscribe { sessionMono.subscribe() }
            .subscribe()

        latch.await()
    }

    /*
    @Test
    fun myTest() {
        val latch = CountDownLatch(20)
        val output = EmitterProcessor.create<String>()
        val input = Flux.generate<String> { sink -> sink.next("This is a test") }
            .delayElements(Duration.ofSeconds(1))

        val client = ReactorNettyWebSocketClient()
        val sessionMono = client.execute(URI.create("ws://echo.websocket.org")) { session ->
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
    }*/
}
