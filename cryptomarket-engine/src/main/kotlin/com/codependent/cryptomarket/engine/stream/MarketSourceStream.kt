package com.codependent.cryptomarket.engine.stream

import com.codependent.cryptomarket.engine.service.MarketService
import org.slf4j.LoggerFactory
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext
import org.springframework.cloud.stream.messaging.Source
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component
import reactor.core.scheduler.Schedulers

@Component
class MarketSourceStream(private val source: Source, private val marketService: MarketService) : ApplicationListener<ContextRefreshedEvent> {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        if (event.source is AnnotationConfigReactiveWebServerApplicationContext) {
            startMarketStream()
        }
    }

    fun startMarketStream() {
        marketService.getMarketStream()
                .subscribeOn(Schedulers.elastic())
                .subscribe {
                    logger.info("Sending {}", it)
                    source.output().send(MessageBuilder.withPayload(it).build())
                }
    }
}