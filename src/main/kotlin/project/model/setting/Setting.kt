package project.model.setting

import jakarta.persistence.*
import project.model.user.User

@Entity
@Table(name = "setting")
data class Setting(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = ForeignKey(name = "fk_setting_user"))
    val user: User,

    @Column(nullable = false, columnDefinition = "TEXT")
    val text: String
)
