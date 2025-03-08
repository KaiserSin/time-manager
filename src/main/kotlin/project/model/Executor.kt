package project.model

import jakarta.persistence.*

@Entity
@Table(name = "executor")
data class Executor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 100)
    val name: String
)
