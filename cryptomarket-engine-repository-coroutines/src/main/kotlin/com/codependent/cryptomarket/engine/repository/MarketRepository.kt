package com.codependent.cryptomarket.engine.repository

import com.codependent.cryptomarket.engine.document.MarketDocument
import org.springframework.data.mongodb.core.*
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.query.Criteria.where

class MarketRepository(private val mongo: ReactiveFluentMongoOperations) {
    fun findAll() =
            mongo.query<MarketDocument>().flow()
    suspend fun findById(id: String) = mongo.query<MarketDocument>()
            .matching(query(where("name").isEqualTo(id))).awaitOne()
    suspend fun insert(marketDocument: MarketDocument): MarketDocument =
            mongo.insert<MarketDocument>().oneAndAwait(marketDocument)
    suspend fun update(marketDocument: MarketDocument): MarketDocument =
            mongo.update<MarketDocument>().replaceWith(marketDocument)
            .asType<MarketDocument>().findReplaceAndAwait()
}
