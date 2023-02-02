package com.basic101.firststep

import com.basic101.firststep.model.CommentEntity
import com.basic101.firststep.model.PostEntity
import com.basic101.firststep.model.UserEntity
import com.basic101.firststep.repository.CommentRepository
import com.basic101.firststep.repository.PostRepository
import com.basic101.firststep.repository.UserRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class FirststepApplication {

	@Bean
	fun runner(
		userRepository: UserRepository,
		postRepository: PostRepository,
		commentRepository: CommentRepository
	): ApplicationRunner {
		return ApplicationRunner {
			val user = UserEntity(
				name = "Test user"
			)

			userRepository.save(user)

			val postEntity = PostEntity(
				title = "Test title",
				description = "Test description",
				author = user
			)

			val postEntity2 = PostEntity(
				title = "Test title - 2",
				description = "Test description - 2",
				author = user
			)

			postRepository.saveAll(listOf(
				postEntity2,
				postEntity
			))

			val comment = CommentEntity(
				text = "testing comment",
				author = user,
				post = postEntity
			)

			commentRepository.save(comment)
		}
	}


}

fun main(args: Array<String>) {
	runApplication<FirststepApplication>(*args)
}




