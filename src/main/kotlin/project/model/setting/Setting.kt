package project.model.setting

import jakarta.persistence.*
import project.model.executor.Executor

@Entity
@Table(name = "setting")
data class Setting(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_executor", nullable = false, foreignKey = ForeignKey(name = "fk_setting_executor"))
    val executor: Executor,

    @Column(nullable = false, columnDefinition = "TEXT")
    val text: String
)