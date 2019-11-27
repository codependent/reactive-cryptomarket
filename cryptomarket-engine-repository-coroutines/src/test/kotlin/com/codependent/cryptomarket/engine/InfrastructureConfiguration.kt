package com.codependent.cryptomarket.engine

import com.codependent.cryptomarket.engine.repository.MarketRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.core.ReactiveFluentMongoOperations

@SpringBootApplication
class InfrastructureConfiguration(private val mongo: ReactiveFluentMongoOperations) {
    @Bean
    fun marketRepository() = MarketRepository(mongo)
}