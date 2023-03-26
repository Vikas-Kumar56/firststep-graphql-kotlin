package com.basic101.firststep.repository

import com.basic101.firststep.model.CommentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface CommentRepository: JpaRepository<CommentEntity, UUID> {
    fun findAllByPostId(postId: UUID): List<CommentEntity>
    fun findAllByAuthorId(authorId: UUID): List<CommentEntity>

    @Query("select c from CommentEntity c where c.post.id in ?1")
    fun findAllByPostIds(postIds: List<UUID>): List<CommentEntity>
}