package com.codependent.cryptomarket.service

import com.codependent.cryptomarket.dto.Market
import reactor.core.publisher.Flux

interface MarketService {

    fun getMarketValues(): Flux<Market>
}