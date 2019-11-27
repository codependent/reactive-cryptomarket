package com.codependent.cryptomarket.engine.document

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class MarketDocument(@Id val name: String, var value: Float)