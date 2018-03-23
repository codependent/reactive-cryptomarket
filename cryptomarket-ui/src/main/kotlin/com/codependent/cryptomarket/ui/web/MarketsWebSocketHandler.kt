package com.codependent.cryptomarket.ui.web

import com.codependent.cryptomarket.ui.stream.MarketSink
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class MarketsWebSocketHandler(val marketSink: MarketSink) : WebSocketHandler {
    override fun handle(session: WebSocketSession): Mono<Void> {
        val flux = Flux.just("Hello", "Jose")
        return session.send(flux
                .map(session::textMessage))
                .and(session.receive()
                        .map { message ->
                            println(message.payloadAsText)
                            message.payloadAsText
                        }
                        .log())
    }
}