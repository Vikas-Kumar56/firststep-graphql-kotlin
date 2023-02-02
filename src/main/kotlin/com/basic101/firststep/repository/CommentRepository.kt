package com.basic101.firststep.repository

import com.basic101.firststep.model.CommentEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CommentRepository: JpaRepository<CommentEntity, UUID> {
    fun findAllByPostId(postId: UUID): List<CommentEntity>
    fun findAllByAuthorId(authorId: UUID): List<CommentEntity>
}