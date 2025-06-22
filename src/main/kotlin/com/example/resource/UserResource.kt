package com.example.resource

import com.example.dto.CreateUserRequest
import com.example.dto.UpdateUserRequest
import com.example.dto.UserResponse
import com.example.service.UserService
import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.eclipse.microprofile.openapi.annotations.tags.Tag

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Users", description = "Operações relacionadas aos usuários")
class UserResource {

    @Inject
    lateinit var userService: UserService

    @GET
    @Operation(summary = "Listar todos os usuários")
    @APIResponse(
        responseCode = "200",
        description = "Lista de usuários",
        content = [Content(schema = Schema(implementation = UserResponse::class))]
    )
    fun getAllUsers(): Response {
        val users = userService.getAllUsers()
        return Response.ok(users).build()
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar usuário por ID")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Usuário encontrado"),
        APIResponse(responseCode = "404", description = "Usuário não encontrado")
    )
    fun getUserById(@PathParam("id") id: String): Response {
        val user = userService.getUserById(id)
        return if (user != null) {
            Response.ok(user).build()
        } else {
            Response.status(Response.Status.NOT_FOUND)
                .entity(mapOf("message" to "Usuário não encontrado"))
                .build()
        }
    }

    @GET
    @Path("/email/{email}")
    @Operation(summary = "Buscar usuário por email")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Usuário encontrado"),
        APIResponse(responseCode = "404", description = "Usuário não encontrado")
    )
    fun getUserByEmail(@PathParam("email") email: String): Response {
        val user = userService.getUserByEmail(email)
        return if (user != null) {
            Response.ok(user).build()
        } else {
            Response.status(Response.Status.NOT_FOUND)
                .entity(mapOf("message" to "Usuário não encontrado"))
                .build()
        }
    }

    @GET
    @Path("/search")
    @Operation(summary = "Pesquisar usuários por nome")
    @APIResponse(responseCode = "200", description = "Lista de usuários encontrados")
    fun searchUsers(@QueryParam("name") name: String?): Response {
        if (name.isNullOrBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(mapOf("message" to "Parâmetro 'name' é obrigatório"))
                .build()
        }

        val users = userService.searchUsersByName(name)
        return Response.ok(users).build()
    }

    @POST
    @Operation(summary = "Criar novo usuário")
    @APIResponses(
        APIResponse(responseCode = "201", description = "Usuário criado com sucesso"),
        APIResponse(responseCode = "400", description = "Dados inválidos"),
        APIResponse(responseCode = "409", description = "Email já está em uso")
    )
    fun createUser(@Valid request: CreateUserRequest): Response {
        return try {
            val user = userService.createUser(request)
            Response.status(Response.Status.CREATED).entity(user).build()
        } catch (e: IllegalArgumentException) {
            Response.status(Response.Status.CONFLICT)
                .entity(mapOf("message" to e.message))
                .build()
        }
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar usuário")
    @APIResponses(
        APIResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
        APIResponse(responseCode = "404", description = "Usuário não encontrado"),
        APIResponse(responseCode = "409", description = "Email já está em uso")
    )
    fun updateUser(@PathParam("id") id: String, @Valid request: UpdateUserRequest): Response {
        return try {
            val user = userService.updateUser(id, request)
            if (user != null) {
                Response.ok(user).build()
            } else {
                Response.status(Response.Status.NOT_FOUND)
                    .entity(mapOf("message" to "Usuário não encontrado"))
                    .build()
            }
        } catch (e: IllegalArgumentException) {
            Response.status(Response.Status.CONFLICT)
                .entity(mapOf("message" to e.message))
                .build()
        }
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Deletar usuário")
    @APIResponses(
        APIResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
        APIResponse(responseCode = "404", description = "Usuário não encontrado")
    )
    fun deleteUser(@PathParam("id") id: String): Response {
        val deleted = userService.deleteUser(id)
        return if (deleted) {
            Response.noContent().build()
        } else {
            Response.status(Response.Status.NOT_FOUND)
                .entity(mapOf("message" to "Usuário não encontrado"))
                .build()
        }
    }
}