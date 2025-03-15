package project.model.executor

import jakarta.persistence.*
import project.model.listtable.ListTable

@Entity
@Table(name = "executor")
data class Executor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 100)
    val name: String? = null,

    @OneToMany(mappedBy = "executor", cascade = [CascadeType.ALL], orphanRemoval = true)
    val tasks: List<ListTable> = emptyList()
)
