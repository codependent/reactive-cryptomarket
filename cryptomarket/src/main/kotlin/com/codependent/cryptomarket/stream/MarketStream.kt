package com.codependent.cryptomarket.stream

import com.codependent.cryptomarket.dto.Market
import com.codependent.cryptomarket.service.MarketService
import org.springframework.cloud.stream.messaging.Source
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.Duration

@Component
class MarketStream(private val source: Source, private val marketService: MarketService) {

    init {
        liveMarket()
    }

    private fun liveMarket() {
        Flux.interval(Duration.ofSeconds(1))
                .map {
                    val marketValues = marketService.getMarketValues()
                    marketValues.subscribe {
                        source.output().send(MessageBuilder.withPayload(Market("",0.0f)).build());
                    }
                }
    }

}