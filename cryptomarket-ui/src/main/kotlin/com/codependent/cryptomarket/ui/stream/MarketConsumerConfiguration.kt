package com.codependent.cryptomarket.ui.stream

import com.codependent.cryptomarket.ui.dto.Market
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks

@Configuration
class MarketConsumerConfiguration {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun marketSink() = Sinks.many().multicast().directBestEffort<Market>()

    @Bean
    fun marketConsumer(): java.util.function.Function<Flux<Market>, Mono<Void>> =
        java.util.function.Function<Flux<Market>, Mono<Void>> {
            it.doOnNext { market ->
                logger.info("Received: {}", market)
                marketSink().tryEmitNext(market)
            }.then()
        }
}

