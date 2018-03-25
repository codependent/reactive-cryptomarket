package com.codependent.cryptomarket.ui.web

import com.codependent.cryptomarket.ui.stream.MarketSink
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter
import java.util.*


@Configuration
class WebSocketConfiguration {

    @Bean
    fun webSocketMapping(marketSink: MarketSink, objectMapper : ObjectMapper): HandlerMapping {
        val map = HashMap<String, WebSocketHandler>()
        map["/markets"] = MarketsWebSocketHandler(marketSink, objectMapper)
        val mapping = SimpleUrlHandlerMapping()
        mapping.urlMap = map
        mapping.order = 10
        return mapping
    }

    @Bean
    fun handlerAdapter(): WebSocketHandlerAdapter {
        return WebSocketHandlerAdapter()
    }

}