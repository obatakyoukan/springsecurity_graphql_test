package com.example.teamA.config

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.teamA.service.SimpleUserDetailService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class SecurityConfig {
    @Value("\${security.secret-key:secret}")
    private var secretKey : String = "secret"

    private val logger : Logger
        = LoggerFactory.getLogger(SecurityConfig::class.java)

    @Bean
    fun jwtAlgorithm() : Algorithm {
        logger.info("create jwt Algorithm in SecurityConfig")
        return Algorithm.HMAC256(secretKey)
    }

    @Bean
    fun verifier(algorithm: Algorithm) : JWTVerifier {
        logger.info("create verifier in SecurityConfig")
        return JWT
            .require(algorithm)
            .withIssuer("my-graphql-api")
            .build()
    }

    @Bean
    fun passwordEncoder() : PasswordEncoder {
        logger.info("create passwordEncoder in SecurityConfig")
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun authenticationProvider(
        simpleUserDetailService : SimpleUserDetailService,
        passwordEncoder: PasswordEncoder
    ): AuthenticationProvider {
        logger.info("create authenticationProvider in SecurityConfig")
        var provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(simpleUserDetailService)
        provider.setPasswordEncoder(passwordEncoder)
        return provider
    }
}