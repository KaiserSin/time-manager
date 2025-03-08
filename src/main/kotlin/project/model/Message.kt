package project.model

import jakarta.persistence.*

@Entity
@Table(name = "message")
data class Message(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "id_executor", nullable = false)
    val user: Executor,

    @Column(nullable = false)
    val sender: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val text: String
)




