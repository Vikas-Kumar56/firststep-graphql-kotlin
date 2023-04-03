package com.basic101.firststep.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    val id: UUID? = null,

    @Column
    val name: String,

    @Column
    val password: String,

    @Column
    val roles: String,

    @OneToMany(mappedBy = "author")
    val posts: Set<PostEntity> = setOf(),

    @OneToMany(mappedBy = "author")
    val comments: Set<CommentEntity> = setOf(),
)