package com.codependent.cryptomarket.service

import com.codependent.cryptomarket.dto.Market
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.UnicastProcessor
import java.time.Duration
import java.util.*

@Component
class MarketService {

    private val markets = mutableMapOf(
            "BTC" to 7000.00f,
            "ETH" to 600.00f)

    val liveMarkets = mutableMapOf<String, UnicastProcessor<Market>>()
    private val random = Random()

    init {
        startLiveMarkets()
    }

    private fun startLiveMarkets() {
        markets.forEach {
            val u : UnicastProcessor<Market> = UnicastProcessor.create()
            u.publish().autoConnect()
            liveMarkets[it.key] = u
        }
        Flux.interval(Duration.ofSeconds(1))
                .map {
                    liveMarkets.forEach { t, u ->
                        markets[t] = markets[t] ?: 0.0f + rand(-10f, 10f)
                        u.onNext(Market(t, markets[t] ?: 0.0f))
                    }
                }
    }

    private fun rand(from: Float, to: Float) : Float {
        return random.nextFloat() * (to - from) + from
    }
}