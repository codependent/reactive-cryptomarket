package com.codependent.cryptomarket.engine.stream

import com.codependent.cryptomarket.engine.dto.Market
import com.codependent.cryptomarket.engine.service.MarketService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import java.util.function.Supplier

@Configuration
class MarketSourceStream(private val marketService: MarketService) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun marketSupplier(): Supplier<Flux<Market>> =
        Supplier<Flux<Market>> {
            marketService.getMarketStream()
                .doOnSubscribe {
                    logger.info("Starting market stream")
                }
                .doOnEach {
                    logger.debug("Publishing {}", it.get())
                }
        }

}
