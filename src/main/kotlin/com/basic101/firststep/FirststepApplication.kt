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
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.crypto.password.PasswordEncoder

@EnableMethodSecurity(securedEnabled = true)
@SpringBootApplication
class FirststepApplication {

	@Profile("!test")
	@Bean
	fun runner(
		userRepository: UserRepository,
		postRepository: PostRepository,
		commentRepository: CommentRepository,
		passwordEncoder: PasswordEncoder
	): ApplicationRunner {
		return ApplicationRunner {
			val user = UserEntity(
				name = "John",
				password = passwordEncoder.encode("pass"),
				roles = "ROLE_ADMIN"
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




