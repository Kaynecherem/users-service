package com.example.interoperability

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun existsByEmail(string: String): Boolean

    fun existsByEmailAndIdNot(string: String, id: Long): Boolean
}