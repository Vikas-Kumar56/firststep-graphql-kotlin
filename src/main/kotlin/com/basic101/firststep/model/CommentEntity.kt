package com.basic101.firststep.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "comments")
class CommentEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    val id: UUID? = null,

    @Column(nullable = false)
    val text: String,

    @ManyToOne
    @JoinColumn(name = "author_id")
    val author: UserEntity,

    @ManyToOne
    @JoinColumn(name = "post_id")
    val post: PostEntity

)