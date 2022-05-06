package com.codependent.cryptomarket.engine

import com.codependent.cryptomarket.engine.document.MarketDocument
import com.codependent.cryptomarket.engine.repository.MarketRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.messaging.Source
import reactor.core.publisher.Mono

@SpringBootApplication
class CryptomarketApplication(private val marketRepository: MarketRepository) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun run(vararg args: String?) {
        marketRepository.findById("BTC")
                .switchIfEmpty(Mono.defer {
                    logger.info("Creating default BTC Market")
                    marketRepository.save(MarketDocument("BTC", 7500.00F))
                })
                .subscribe()
        marketRepository.findById("ETH")
                .switchIfEmpty(Mono.defer {
                    logger.info("Creating default ETH Market")
                    marketRepository.save(MarketDocument("ETH", 550.00F))
                })
                .subscribe()
    }
}

fun main(args: Array<String>) {
    runApplication<CryptomarketApplication>(*args)
}
