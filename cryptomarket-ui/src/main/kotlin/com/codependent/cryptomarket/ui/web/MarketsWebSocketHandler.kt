package com.codependent.cryptomarket.ui.web

import com.codependent.cryptomarket.ui.stream.MarketSink
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

class MarketsWebSocketHandler(val marketSink: MarketSink) : WebSocketHandler {
    override fun handle(session: WebSocketSession): Mono<Void> {
        return session.send {  }.then()
    }
}