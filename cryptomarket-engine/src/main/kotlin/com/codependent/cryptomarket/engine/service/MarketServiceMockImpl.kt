package com.codependent.cryptomarket.engine.service

import com.codependent.cryptomarket.engine.dto.Market
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux
import java.time.Duration
import java.util.*

@Service
class MarketServiceImpl : MarketService {

    private val random = Random()
    private val markets = mutableMapOf(
            "BTC" to 7000.00f,
            "ETH" to 600.00f)

    override fun getMarketStream(): Flux<Market> {
        return Flux.interval(Duration.ofSeconds(1))
                .flatMap {
                    markets.map {
                        val currentMarket = Market(it.key, (it.value + rand(-10f, 10f)), Date())
                        currentMarket
                    }.toFlux()
                }.log()
    }

    private fun rand(from: Float, to: Float): Float {
        return random.nextFloat() * (to - from) + from
    }

}