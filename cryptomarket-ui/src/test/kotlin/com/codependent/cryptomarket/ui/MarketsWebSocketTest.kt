package com.codependent.cryptomarket.ui

import org.junit.Test
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient
import java.net.URI


class MarketsWebSocketTest {

    @Test
    fun shouldInvokeWebSocket() {
        val client = ReactorNettyWebSocketClient()
        client.execute(URI("ws://localhost:8080/markets")) {
            it.receive()
                    .doOnNext { println(it) }
                    .then()
        }
    }

}