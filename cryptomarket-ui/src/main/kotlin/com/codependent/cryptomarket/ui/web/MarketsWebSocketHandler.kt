package com.codependent.cryptomarket.ui.web

import com.codependent.cryptomarket.ui.dto.Market
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks

class MarketsWebSocketHandler(private val marketSink: Sinks.Many<Market>, private val jacksonMapper: ObjectMapper) : WebSocketHandler {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun handle(session: WebSocketSession): Mono<Void> {

        val webSocketSink = Sinks.many().multicast().directBestEffort<Market>()
        val webSocketSinkFlux = webSocketSink.asFlux()

        session.receive().doFinally {
            logger.info("Receive session finished - reason [{}]", it.name)
            session.close()
        }.map { message ->
            message.payloadAsText
        }.log().subscribe { marketId ->
            marketSink.asFlux().filter{ it.name == marketId}
                .subscribe {market ->
                    webSocketSink.tryEmitNext(market)
                }
        }

        return session.send(
            webSocketSinkFlux
                .doOnNext { logger.info("Relaying [{}]", it) }
                .map { session.textMessage(jacksonMapper.writeValueAsString(it)) }
                .log()
                .doFinally { logger.info("Send session finished - reason [{}]", it.name) }

        ).then()
    }
}
