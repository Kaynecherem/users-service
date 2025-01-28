package com.example.interoperability

import com.fasterxml.jackson.databind.ObjectMapper
import java.time.Instant
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

@Configuration
@EnableJms
class JmsConfig

@Component
class JmsTemplateAdapter(
    private val objectMapper: ObjectMapper,
    private val jmsTemplate: JmsTemplate,
) {
    fun sendMessage(
        method: HttpMethod,
        url: String,
        requestBody: User? = null,
        responseBody: Any? = null
    ) {
        val writer = objectMapper.writer()
        jmsTemplate.convertAndSend(
            "${Instant.now()} :: ${method.name()} $url\n" +
                (if (requestBody != null) "Request: ${writer.writeValueAsString(requestBody)}\n" else "") +
                "Response: ${writer.writeValueAsString(responseBody)}"
        )
    }
}