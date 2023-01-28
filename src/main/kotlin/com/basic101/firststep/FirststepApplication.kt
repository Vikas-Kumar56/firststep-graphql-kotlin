package com.basic101.firststep

import com.basic101.firststep.model.PostEntity
import com.basic101.firststep.model.UserEntity
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
		postRepository: PostRepository
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

			postRepository.save(postEntity)
		}
	}


}

fun main(args: Array<String>) {
	runApplication<FirststepApplication>(*args)
}




