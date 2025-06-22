package com.example.entity

import io.quarkus.mongodb.panache.kotlin.PanacheMongoEntity
import io.quarkus.mongodb.panache.kotlin.PanacheMongoCompanion
import io.quarkus.mongodb.panache.common.MongoEntity
import java.time.LocalDateTime

@MongoEntity(collection = "users")
data class User(
    var name: String = "",
    var email: String = "",
    var age: Int = 0,
    var createdAt: LocalDateTime = LocalDateTime.now()
) : PanacheMongoEntity() {

    companion object : PanacheMongoCompanion<User> {
        fun findByEmail(email: String): User? = find("email", email).firstResult()
        fun findByNameContaining(name: String): List<User> = find("name like ?1", "%$name%").list()
    }
}