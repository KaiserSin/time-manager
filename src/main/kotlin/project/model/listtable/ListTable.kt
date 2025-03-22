package project.model.listtable

import jakarta.persistence.*
import project.model.user.User
import project.model.task.Task

@Entity
@Table(name = "list")
data class ListTable(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    val task: Task,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User
)
