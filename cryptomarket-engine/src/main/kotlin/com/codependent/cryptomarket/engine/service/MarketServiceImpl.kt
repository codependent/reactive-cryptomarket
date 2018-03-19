package com.codependent.cryptomarket.engine.service

import com.codependent.cryptomarket.engine.dto.Market
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.UnicastProcessor
import reactor.core.scheduler.Schedulers
import java.time.Duration
import java.util.*

@Service
class MarketServiceImpl : MarketService, ApplicationListener<ContextRefreshedEvent>{

    private val logger = LoggerFactory.getLogger(javaClass)
    private val random = Random()
    private val markets = mutableMapOf(
            "BTC" to 7000.00f,
            "ETH" to 600.00f)

    private val emitter = UnicastProcessor.create<Market>()
    private val marketFlux: Flux<Market> = emitter.publish().autoConnect()

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        startLiveMarkets()
    }

    override fun getMarketStream(): Flux<Market> {
        return marketFlux
    }

    private fun startLiveMarkets() {
        Flux.interval(Duration.ofSeconds(5))
                .map {
                    markets.forEach { n, v ->
                        val currentMarket = (Market(n, (v + rand(-10f, 10f))))
                        logger.info("Emmiting {}", currentMarket)
                        emitter.onNext(currentMarket)
                    }
                }
                .subscribeOn(Schedulers.elastic())
                .subscribe()
    }

    private fun rand(from: Float, to: Float): Float {
        return random.nextFloat() * (to - from) + from
    }
}