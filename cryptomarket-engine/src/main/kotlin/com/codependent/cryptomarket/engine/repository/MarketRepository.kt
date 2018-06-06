package com.codependent.cryptomarket.engine.repository

import com.codependent.cryptomarket.engine.document.MarketDocument
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface MarketRepository : ReactiveMongoRepository<MarketDocument, String>