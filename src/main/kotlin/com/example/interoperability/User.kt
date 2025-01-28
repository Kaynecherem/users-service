package com.example.interoperability

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotEmpty

@NoArg
@Entity
@Table(name = "app_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @NotEmpty(message = "Name is required")
    @Column(nullable = false)
    var name: String,

    @NotEmpty(message = "Email should be valid")
    @Column(unique = true, nullable = false)
    var email: String
)