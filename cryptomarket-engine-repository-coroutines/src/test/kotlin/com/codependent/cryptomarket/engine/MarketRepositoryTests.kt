package com.codependent.cryptomarket.engine

import com.codependent.cryptomarket.engine.document.MarketDocument
import com.codependent.cryptomarket.engine.repository.MarketRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest(classes = [InfrastructureConfiguration::class])
class CustomerRepositoryIntegrationTests(@Autowired private val marketRepository: MarketRepository) {
    private val random = Random()

    @Test
    fun test() {
        val marketDocument = MarketDocument("name", 1f)
        runBlocking<Unit> {
            marketRepository.insert(marketDocument)
            var actual = marketRepository.findById("name")
            actual.value += rand(-10f, 10f)
            actual = marketRepository.update(actual)
            marketRepository.findAll().collect { value -> Assertions.assertEquals(marketDocument.name, actual.name) }
        }

    }

    private fun rand(from: Float, to: Float): Float {
        return random.nextFloat() * (to - from) + from
    }

}