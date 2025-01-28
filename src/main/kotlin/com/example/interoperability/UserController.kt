package com.example.interoperability

import jakarta.validation.Valid
import org.springframework.http.HttpMethod.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
@Validated
class UserController(
    private val userRepository: UserRepository,
    private val jmsTemplate: JmsTemplateAdapter
) {

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(userRepository.findAll())
            .also { jmsTemplate.sendMessage(GET, "/api/users", null, it) }
    }

    @GetMapping("/{id}")
    fun getUserByID(@PathVariable id: Long): ResponseEntity<User> {
        val user = userRepository.findById(id)
        val response = if (user.isPresent) {
            ResponseEntity.ok(user.get())
        } else {
            ResponseEntity.notFound().build()
        }

        return response.also { jmsTemplate.sendMessage(GET, "/api/users/$id", null, it) }
    }

    @PostMapping
    fun createUser(@Valid @RequestBody user: User): ResponseEntity<User> {
        val response = if (userRepository.existsByEmail(user.email)) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        } else {
            ResponseEntity.status(HttpStatus.CREATED).body(userRepository.save(user))
        }

        return response.also { jmsTemplate.sendMessage(POST, "/api/users", user, it) }
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        val response: ResponseEntity<Void> = if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }

        return response.also { jmsTemplate.sendMessage(DELETE, "/api/users/$id", null, null) }
    }

    @PutMapping("/{id}")
    fun updateUserEmail(@PathVariable id: Long, @RequestBody update: User): ResponseEntity<User> {
        val user = userRepository.findById(id)
        val response = if (user.isPresent) {
            if (userRepository.existsByEmailAndIdNot(update.email, user.get().id!!)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build()
            }

            val updateUser = user.get().apply {
                name = update.name
                email = update.email
            }
            ResponseEntity.ok(userRepository.save(updateUser))
        } else {
            ResponseEntity.notFound().build()
        }

        return response.also { jmsTemplate.sendMessage(PUT, "/api/users/$id", update, it) }
    }
}