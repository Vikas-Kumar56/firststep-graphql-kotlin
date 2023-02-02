package com.basic101.firststep.repository

import com.basic101.firststep.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository: JpaRepository<UserEntity, UUID> {
    fun findByPostsId(postId: UUID): UserEntity
    fun findByCommentsId(commentId: UUID): UserEntity
}