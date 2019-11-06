package com.codependent.cryptomarket.engine.service

import com.codependent.cryptomarket.engine.document.MarketDocument
import com.codependent.cryptomarket.engine.dto.Market
import com.codependent.cryptomarket.engine.repository.MarketRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.BDDMockito
import org.mockito.Mockito
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Duration
import java.util.*


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MarketServiceImplTests {
    private val marketRepository = Mockito.mock(MarketRepository::class.java)
    private val marketService = MarketServiceImpl(marketRepository)

    @BeforeEach
    fun beforeEach() {
        Mockito.reset(marketRepository)
    }

    @Test
    fun getMarketStream() {
        val marketDocument = MarketDocument("hola", 2.5f)
        val marketDocuments = Flux.just(marketDocument)
        BDDMockito.given(marketRepository.findAll()).willReturn(marketDocuments)
        BDDMockito.given(marketRepository.save(Mockito.any(MarketDocument::class.java))).willReturn(Mono.just(marketDocument))

        val market = Market(marketDocument.name, marketDocument.value, Date())
        StepVerifier
                .withVirtualTime { marketService.getMarketStream().take(1) }
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(1))
                .expectNextMatches {
                    it.timestamp != null &&
                            it.value != null &&
                            it.name == market.name
                }
                .verifyComplete()
    }
}
