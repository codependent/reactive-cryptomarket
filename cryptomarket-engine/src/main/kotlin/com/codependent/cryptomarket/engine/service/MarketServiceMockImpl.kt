package com.codependent.cryptomarket.engine.service

import com.codependent.cryptomarket.engine.dto.Market
import com.codependent.cryptomarket.engine.repository.MarketRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.*

@Service
class MarketServiceImpl(private val marketRepository: MarketRepository) : MarketService {

    private val random = Random()

    override fun getMarketStream(): Flux<Market> {
        return Flux.interval(Duration.ofSeconds(1))
                .flatMap {
                    marketRepository.findAll().flatMap {
                        it.value += rand(-100f, 100f)
                        marketRepository.save(it)
                    }.map {
                        Market(it.name, it.value, Date())
                    }
                }
    }

    private fun rand(from: Float, to: Float): Float {
        return random.nextFloat() * (to - from) + from
    }

}
