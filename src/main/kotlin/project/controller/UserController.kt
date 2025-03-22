package project.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import project.model.user.dto.UserRequest
import project.model.user.dto.UserResponse
import project.service.UserService
import project.util.OnCreate
import project.util.OnUpdate
import project.util.toResponse

@RestController
@RequestMapping("/executors")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    fun createExecutor(
        @Validated(OnCreate::class) @RequestBody userRequest: UserRequest
    ): ResponseEntity<UserResponse> {
        val createdExecutor = userService.createUser(userRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdExecutor.toResponse())
    }

    @DeleteMapping("/{id}")
    fun deleteExecutor(@PathVariable id: Long): ResponseEntity<String> {
        userService.deleteUser(id)
        return ResponseEntity.ok("User with ID $id has been successfully deleted.")
    }

    @GetMapping("/{id}")
    fun getExecutorById(@PathVariable id: Long): ResponseEntity<UserResponse> {
        val executor = userService.getUserByIdOrThrow(id)
        return ResponseEntity.ok(executor.toResponse())
    }

    @GetMapping
    fun getAllExecutors(
        @PageableDefault(size = 10) pageable: Pageable
    ): ResponseEntity<Page<UserResponse>> {
        val executorPage = userService.getAllUsers(pageable)
        val responsePage = executorPage.map { it.toResponse() }
        return ResponseEntity.ok(responsePage)
    }

    @PutMapping("/{id}")
    fun updateExecutor(
        @PathVariable id: Long,
        @Validated(OnUpdate::class) @RequestBody userRequest: UserRequest
    ): ResponseEntity<UserResponse> {
        val updatedExecutor = userService.updateUser(id, userRequest)
        return ResponseEntity.ok(updatedExecutor.toResponse())
    }
}
