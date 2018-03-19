package com.codependent.cryptomarket.engine.service

import com.codependent.cryptomarket.engine.dto.Market
import reactor.core.publisher.Flux

interface MarketService {

    fun getMarketStream(): Flux<Market>

}