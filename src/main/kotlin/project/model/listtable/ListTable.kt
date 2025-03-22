package project.model.listtable

import jakarta.persistence.*
import project.model.task.Task
import project.model.user.User

@Entity
@Table(name = "list")
data class ListTable(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    val task: Task,

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User
)
