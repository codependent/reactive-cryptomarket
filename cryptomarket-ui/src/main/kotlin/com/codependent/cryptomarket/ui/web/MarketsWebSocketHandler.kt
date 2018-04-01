package com.codependent.cryptomarket.ui.web

import com.codependent.cryptomarket.ui.dto.Market
import com.codependent.cryptomarket.ui.stream.MarketSink
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Mono

class MarketsWebSocketHandler(private val marketSink: MarketSink, private val jacksonMapper: ObjectMapper) : WebSocketHandler {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun handle(session: WebSocketSession): Mono<Void> {
        val processor = EmitterProcessor.create<Market>()

        session.receive().doFinally {
            logger.info("Receive session finished - reason [{}]", it.name)
            session.close()
        }.map { message ->
            message.payloadAsText
        }.log().subscribe {
            marketSink.getMarket(it).subscribeWith(processor)
        }

        return session.send(processor.doOnNext {
            logger.info("Relaying [{}]", it)
        }.map {
            session.textMessage(jacksonMapper.writeValueAsString(it))
        }).log().doFinally {
            logger.info("Send session finished - reason [{}]", it.name)
        }.then()
    }
}