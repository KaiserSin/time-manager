package project.model

import jakarta.persistence.*
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
    @JoinColumn(name = "executor_id", nullable = false)
    val executor: Executor
)
