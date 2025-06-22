package com.example.resource

import com.example.dto.CreateUserRequest
import com.example.dto.UpdateUserRequest
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.mongodb.MongoTestResource
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.CoreMatchers.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation

@QuarkusTest
@QuarkusTestResource(MongoTestResource::class)
@TestMethodOrder(OrderAnnotation::class)
class UserResourceTest {

    @Test
    @Order(1)
    fun `should create a new user`() {
        val createUserRequest = CreateUserRequest(
            name = "Test User",
            email = "test@example.com",
            age = 25
        )

        given()
            .contentType(ContentType.JSON)
            .body(createUserRequest)
            .`when`()
            .post("/api/users")
            .then()
            .statusCode(201)
            .body("name", equalTo("Test User"))
            .body("email", equalTo("test@example.com"))
            .body("age", equalTo(25))
            .body("id", notNullValue())
            .body("created_at", notNullValue())
    }

    @Test
    @Order(2)
    fun `should not create user with duplicate email`() {
        val createUserRequest = CreateUserRequest(
            name = "Another User",
            email = "test@example.com", // Email já usado no teste anterior
            age = 30
        )

        given()
            .contentType(ContentType.JSON)
            .body(createUserRequest)
            .`when`()
            .post("/api/users")
            .then()
            .statusCode(409)
            .body("message", containsString("Email já está em uso"))
    }

    @Test
    @Order(3)
    fun `should validate user creation with invalid data`() {
        val invalidUserRequest = CreateUserRequest(
            name = "", // Nome vazio
            email = "invalid-email", // Email inválido
            age = 0 // Idade inválida
        )

        given()
            .contentType(ContentType.JSON)
            .body(invalidUserRequest)
            .`when`()
            .post("/api/users")
            .then()
            .statusCode(400)
    }

    @Test
    @Order(4)
    fun `should list all users`() {
        given()
            .`when`()
            .get("/api/users")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
    }

    @Test
    @Order(5)
    fun `should find user by email`() {
        given()
            .`when`()
            .get("/api/users/email/test@example.com")
            .then()
            .statusCode(200)
            .body("name", equalTo("Test User"))
            .body("email", equalTo("test@example.com"))
    }

    @Test
    @Order(6)
    fun `should return 404 for non-existent email`() {
        given()
            .`when`()
            .get("/api/users/email/nonexistent@example.com")
            .then()
            .statusCode(404)
            .body("message", equalTo("Usuário não encontrado"))
    }

    @Test
    @Order(7)
    fun `should search users by name`() {
        given()
            .queryParam("name", "Test")
            .`when`()
            .get("/api/users/search")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("[0].name", containsString("Test"))
    }

    @Test
    @Order(8)
    fun `should return bad request when searching without name parameter`() {
        given()
            .`when`()
            .get("/api/users/search")
            .then()
            .statusCode(400)
            .body("message", equalTo("Parâmetro 'name' é obrigatório"))
    }

    @Test
    @Order(9)
    fun `should update user`() {
        // Primeiro, obter um usuário existente
        val users = given()
            .`when`()
            .get("/api/users")
            .then()
            .statusCode(200)
            .extract()
            .path<List<Map<String, Any>>>("$")

        val userId = users.first()["id"] as String

        val updateRequest = UpdateUserRequest(
            name = "Updated Test User",
            age = 26
        )

        given()
            .contentType(ContentType.JSON)
            .body(updateRequest)
            .`when`()
            .put("/api/users/$userId")
            .then()
            .statusCode(200)
            .body("name", equalTo("Updated Test User"))
            .body("age", equalTo(26))
    }

    @Test
    @Order(10)
    fun `should return 404 when updating non-existent user`() {
        val updateRequest = UpdateUserRequest(
            name = "Updated Name"
        )

        given()
            .contentType(ContentType.JSON)
            .body(updateRequest)
            .`when`()
            .put("/api/users/507f1f77bcf86cd799439011") // ObjectId válido mas inexistente
            .then()
            .statusCode(404)
            .body("message", equalTo("Usuário não encontrado"))
    }

    @Test
    @Order(11)
    fun `should delete user`() {
        // Primeiro, criar um usuário para deletar
        val createUserRequest = CreateUserRequest(
            name = "User to Delete",
            email = "delete@example.com",
            age = 30
        )

        val userId = given()
            .contentType(ContentType.JSON)
            .body(createUserRequest)
            .`when`()
            .post("/api/users")
            .then()
            .statusCode(201)
            .extract()
            .path<String>("id")

        // Agora deletar o usuário
        given()
            .`when`()
            .delete("/api/users/$userId")
            .then()
            .statusCode(204)

        // Verificar se foi realmente deletado
        given()
            .`when`()
            .get("/api/users/$userId")
            .then()
            .statusCode(404)
    }

    @Test
    @Order(12)
    fun `should return 404 when deleting non-existent user`() {
        given()
            .`when`()
            .delete("/api/users/507f1f77bcf86cd799439011") // ObjectId válido mas inexistente
            .then()
            .statusCode(404)
            .body("message", equalTo("Usuário não encontrado"))
    }

    @Test
    @Order(13)
    fun `should handle invalid ObjectId format`() {
        given()
            .`when`()
            .get("/api/users/invalid-id")
            .then()
            .statusCode(404)
            .body("message", equalTo("Usuário não encontrado"))
    }
}