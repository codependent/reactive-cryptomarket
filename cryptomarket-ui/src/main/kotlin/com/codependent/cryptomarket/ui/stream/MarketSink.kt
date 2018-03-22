package com.codependent.cryptomarket.ui.stream

import com.codependent.cryptomarket.ui.dto.Market
import org.slf4j.LoggerFactory
import org.springframework.cloud.stream.annotation.Input
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.cloud.stream.messaging.Sink
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.UnicastProcessor
import reactor.core.scheduler.Schedulers


@Component
class MarketSink {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val emitter = UnicastProcessor.create<Market>()
    val marketFlux: Flux<Market> = emitter.publish().autoConnect()

    @StreamListener
    fun handle(@Input(Sink.INPUT) market: Flux<Market>) {
        market.subscribeOn(Schedulers.elastic())
                .subscribe {
                    logger.info("Received: {}", it)
                    emitter.onNext(it)
                }

    }

}