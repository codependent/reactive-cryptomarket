package com.codependent.cryptomarket.engine.stream

import com.codependent.cryptomarket.engine.dto.Market
import com.codependent.cryptomarket.engine.service.MarketService
import org.slf4j.LoggerFactory
import org.springframework.cloud.stream.annotation.Output
import org.springframework.cloud.stream.messaging.Source
import org.springframework.cloud.stream.reactive.StreamEmitter
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class MarketSourceStream(private val marketService: MarketService) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @StreamEmitter
    @Output(Source.OUTPUT)
    fun startMarketStream(): Flux<Market> {
        logger.info("Starting market stream")
        return marketService.getMarketStream()
    }
}