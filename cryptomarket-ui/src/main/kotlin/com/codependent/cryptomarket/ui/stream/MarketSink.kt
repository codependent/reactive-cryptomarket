package com.codependent.cryptomarket.ui.stream

import com.codependent.cryptomarket.ui.dto.Market
import org.slf4j.LoggerFactory
import org.springframework.cloud.stream.annotation.Input
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.cloud.stream.messaging.Sink
import org.springframework.cloud.stream.messaging.Sink.INPUT
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.UnicastProcessor

@Component
class MarketSink {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val processor = UnicastProcessor.create<Market>()
    val marketFlux: Flux<Market> = processor.publish().autoConnect()

    fun getMarket(market: String): Flux<Market> {
        return marketFlux.filter { it.name == market }
    }

    @StreamListener
    fun handle(@Input(INPUT) market: Flux<Market>) {
        market.subscribe {
            logger.info("Received: {}", it)
            processor.onNext(it)
        }

    }

}
