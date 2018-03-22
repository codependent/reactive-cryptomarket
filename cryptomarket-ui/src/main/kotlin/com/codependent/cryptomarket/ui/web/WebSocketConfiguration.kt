package com.codependent.cryptomarket.ui.web

import com.codependent.cryptomarket.ui.stream.MarketSink
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
    fun webSocketMapping(marketSink: MarketSink): HandlerMapping {
        val map = HashMap<String, WebSocketHandler>()
        map["/markets"] = MarketsWebSocketHandler(marketSink)
        val mapping = SimpleUrlHandlerMapping()
        mapping.urlMap = map
        return mapping
    }

    @Bean
    fun handlerAdapter(): WebSocketHandlerAdapter {
        return WebSocketHandlerAdapter()
    }

}