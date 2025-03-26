package project.model.user

import jakarta.persistence.*
import project.model.listtable.ListTable

@Entity
@Table(name = "app_user") // соответствует твоему SQL
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 100)
    var name: String? = null,

    @Column(nullable = false, unique = true, length = 255)
    var email: String,

    @Column(nullable = false, length = 255)
    var password: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    var role: Role = Role.USER,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val tasks: List<ListTable> = emptyList()
)