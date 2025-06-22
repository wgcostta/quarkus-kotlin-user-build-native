package com.example.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class CreateUserRequest(
    @field:NotBlank(message = "Nome é obrigatório")
    @field:Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    val name: String,

    @field:NotBlank(message = "Email é obrigatório")
    @field:Email(message = "Email deve ter um formato válido")
    val email: String,

    @field:Min(value = 1, message = "Idade deve ser maior que 0")
    val age: Int
)

data class UpdateUserRequest(
    @field:Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    val name: String?,

    @field:Email(message = "Email deve ter um formato válido")
    val email: String?,

    @field:Min(value = 1, message = "Idade deve ser maior que 0")
    val age: Int?
)

data class UserResponse(
    val id: String,
    val name: String,
    val email: String,
    val age: Int,
    @JsonProperty("created_at")
    val createdAt: LocalDateTime
)