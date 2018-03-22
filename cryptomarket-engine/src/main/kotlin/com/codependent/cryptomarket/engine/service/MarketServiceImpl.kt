package com.codependent.cryptomarket.engine.service

import com.codependent.cryptomarket.engine.dto.Market
import org.slf4j.LoggerFactory
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.UnicastProcessor
import java.time.Duration
import java.util.*

@Service
class MarketServiceImpl : MarketService, ApplicationListener<ContextRefreshedEvent> {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val random = Random()
    private val markets = mutableMapOf(
            "BTC" to 7000.00f,
            "ETH" to 600.00f)

    private val emitter = UnicastProcessor.create<Market>()
    private val marketFlux: Flux<Market> = emitter.publish().autoConnect()

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        if (event.source is AnnotationConfigReactiveWebServerApplicationContext) {
            startLiveMarkets()
        }
    }

    override fun getMarketStream(): Flux<Market> {
        return marketFlux.log()
    }

    private fun startLiveMarkets() {
        Flux.interval(Duration.ofSeconds(1))
                .map {
                    markets.forEach { n, v ->
                        val currentMarket = (Market(n, (v + rand(-10f, 10f))))
                        logger.info("Emmiting {}", currentMarket)
                        emitter.onNext(currentMarket)
                    }
                }
                .subscribe()
    }

    private fun rand(from: Float, to: Float): Float {
        return random.nextFloat() * (to - from) + from
    }
}