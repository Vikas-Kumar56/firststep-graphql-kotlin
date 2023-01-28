package com.basic101.firststep.repository

import com.basic101.firststep.model.PostEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PostRepository : JpaRepository<PostEntity, UUID> {
    fun findAllByAuthor_Id(authorId: UUID): List<PostEntity>
}