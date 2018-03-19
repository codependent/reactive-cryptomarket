package com.codependent.cryptomarket.ui.stream

import com.codependent.cryptomarket.ui.dto.Market
import org.slf4j.LoggerFactory
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.cloud.stream.messaging.Sink
import org.springframework.stereotype.Component


@Component
class MarketSink {

    private val logger = LoggerFactory.getLogger(javaClass)

    @StreamListener(Sink.INPUT)
    fun handle(market: Market) {
        logger.info("Received: {}", market)
    }

}