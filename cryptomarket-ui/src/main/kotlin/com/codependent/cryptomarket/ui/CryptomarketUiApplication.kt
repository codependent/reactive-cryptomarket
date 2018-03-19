package com.codependent.cryptomarket.ui

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.messaging.Sink

@EnableBinding(Sink::class)
@SpringBootApplication
class CryptomarketUiApplication

fun main(args: Array<String>) {
    runApplication<CryptomarketUiApplication>(*args)
}
