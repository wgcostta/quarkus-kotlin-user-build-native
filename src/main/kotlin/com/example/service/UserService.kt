package com.example.service

import com.example.dto.CreateUserRequest
import com.example.dto.UpdateUserRequest
import com.example.dto.UserResponse
import com.example.entity.User
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.bson.types.ObjectId
import org.jboss.logging.Logger

@ApplicationScoped
class UserService {

    private val logger = Logger.getLogger(UserService::class.java)

    fun getAllUsers(): List<UserResponse> {
        logger.info("Buscando todos os usuários")
        return User.findAll().list().map { it.toResponse() }
    }

    fun getUserById(id: String): UserResponse? {
        logger.info("Buscando usuário por ID: $id")
        return try {
            User.findById(ObjectId(id))?.toResponse()
        } catch (e: IllegalArgumentException) {
            logger.warn("ID inválido fornecido: $id")
            null
        }
    }

    fun getUserByEmail(email: String): UserResponse? {
        logger.info("Buscando usuário por email: $email")
        return User.findByEmail(email)?.toResponse()
    }

    fun searchUsersByName(name: String): List<UserResponse> {
        logger.info("Buscando usuários por nome contendo: $name")
        return User.findByNameContaining(name).map { it.toResponse() }
    }

    @Transactional
    fun createUser(request: CreateUserRequest): UserResponse {
        logger.info("Criando novo usuário: ${request.email}")

        // Verificar se email já existe
        User.findByEmail(request.email)?.let {
            throw IllegalArgumentException("Email já está em uso")
        }

        val user = User(
            name = request.name,
            email = request.email,
            age = request.age
        )

        user.persist()
        logger.info("Usuário criado com sucesso: ${user.id}")
        return user.toResponse()
    }

    @Transactional
    fun updateUser(id: String, request: UpdateUserRequest): UserResponse? {
        logger.info("Atualizando usuário: $id")

        val user = try {
            User.findById(ObjectId(id))
        } catch (e: IllegalArgumentException) {
            logger.warn("ID inválido fornecido: $id")
            return null
        } ?: return null

        // Verificar se o novo email já está em uso por outro usuário
        request.email?.let { newEmail ->
            if (newEmail != user.email) {
                User.findByEmail(newEmail)?.let {
                    throw IllegalArgumentException("Email já está em uso")
                }
            }
        }

        request.name?.let { user.name = it }
        request.email?.let { user.email = it }
        request.age?.let { user.age = it }

        user.update()
        logger.info("Usuário atualizado com sucesso: ${user.id}")
        return user.toResponse()
    }

    @Transactional
    fun deleteUser(id: String): Boolean {
        logger.info("Deletando usuário: $id")
        return try {
            val user = User.findById(ObjectId(id))
            if (user != null) {
                user.delete()
                logger.info("Usuário deletado com sucesso: $id")
                true
            } else {
                logger.warn("Usuário não encontrado para deleção: $id")
                false
            }
        } catch (e: IllegalArgumentException) {
            logger.warn("ID inválido fornecido: $id")
            false
        }
    }

    private fun User.toResponse(): UserResponse {
        return UserResponse(
            id = this.id.toString(),
            name = this.name,
            email = this.email,
            age = this.age,
            createdAt = this.createdAt
        )
    }
}