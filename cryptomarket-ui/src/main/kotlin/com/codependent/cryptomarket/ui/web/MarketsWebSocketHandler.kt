package com.codependent.cryptomarket.ui.web

import com.codependent.cryptomarket.ui.dto.Market
import com.codependent.cryptomarket.ui.stream.MarketSink
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Mono

class MarketsWebSocketHandler(val marketSink: MarketSink) : WebSocketHandler {

    private val output = mutableMapOf<WebSocketSession, EmitterProcessor<Market>>()

    override fun handle(session: WebSocketSession): Mono<Void> {
        val processor = EmitterProcessor.create<Market>()
        session.receive()
                .map { message ->
                    message.payloadAsText
                }.log().subscribe{
                    marketSink.getMarket(it).subscribeWith(processor)
                }

        return session.send(processor
                .map {
                    session.textMessage(it.value.toString())
                }).then()
    }
}