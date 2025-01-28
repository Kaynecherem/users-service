package com.example.interoperability

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataInitializer(private val userRepository: UserRepository) {

    @Bean
    fun run() = ApplicationRunner {
        userRepository.saveAll(
            listOf(
                User(name = "John Doe", email = "john.doe@example.com"),
                User(name = "Alice Johnson", email = "alice.johnson@example.com"),
                User(name = "Bob Brown", email = "bob.brown@example.com"),
                User(name = "Charlie Davis", email = "charlie.davis@example.com"),
                User(name = "Emily Evans", email = "emily.evans@example.com"),
                User(name = "Frank Foster", email = "frank.foster@example.com"),
                User(name = "Grace Green", email = "grace.green@example.com"),
                User(name = "Hannah Harris", email = "hannah.harris@example.com"),
                User(name = "Ian Irving", email = "ian.irving@example.com"),
                User(name = "Jackie Jackson", email = "jackie.jackson@example.com")
            )
        )
    }
}