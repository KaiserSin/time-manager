package project.model.task

import jakarta.persistence.*
import project.model.ListTable
import java.time.Duration
import java.time.LocalDateTime


@Entity
@Table(name = "task")
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 100)
    val name: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val description: String,

    @Column(name = "start_time", nullable = false)
    val startTime: LocalDateTime,

    @Column(name = "duration", nullable = false)
    @Convert(converter = DurationConverter::class)
    var duration: Duration,

    @Column(name = "is_done", nullable = false)
    val isDone: Boolean = false,
    @OneToMany(mappedBy = "task", cascade = [CascadeType.ALL], orphanRemoval = true)
    val executors: List<ListTable> = mutableListOf()
)

