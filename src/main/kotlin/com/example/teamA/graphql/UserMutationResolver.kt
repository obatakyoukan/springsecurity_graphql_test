package com.example.teamA.graphql

import com.auth0.jwt.JWT
import com.example.teamA.config.SecurityConfig
import com.example.teamA.entity.ResponseMessage
import com.example.teamA.entity.SimpleLoginUser
import com.example.teamA.service.SimpleUserDetailService
import com.example.teamA.service.UserService
import graphql.kickstart.tools.GraphQLMutationResolver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit


@Component
class UserMutationResolver : GraphQLMutationResolver {
    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var simpleUserDetailService: SimpleUserDetailService
    @Autowired
    private lateinit var authenticationProvider : AuthenticationProvider

    private val logger : Logger
        = LoggerFactory.getLogger(UserMutationResolver::class.java)

    fun signup(name : String, email : String, password : String ): ResponseMessage? {
        logger.info(email)
        userService.createUser(name,email,password)
        return ResponseMessage("ok")
    }

    @PreAuthorize("isAnonymous()")
    fun login(email: String, password: String) : ResponseMessage? {
        val credentials = UsernamePasswordAuthenticationToken(email, password)
        try {
            //SecurityContextHolder.getContext().authentication = authenticationProvider.authenticate(credentials)
            //simpleUserDetailService.getCurrentUser()
            val auth = authenticationProvider.authenticate(credentials)
            val token = generateToken(auth)
            logger.info("generated token : {}", token)
            logger.info("end login")
            //return ResponseMessage("ok")
            return ResponseMessage("Authorization: Bearer %s".format(token) )
        } catch (ex: AuthenticationException) {
            throw BadCredentialsException(email)
        }
    }

    private val EXPIRATION_TIME : Long = TimeUnit.MINUTES.toMillis(10L)

    private fun generateToken( auth : Authentication) : String {
        var simpleLoginUser : SimpleLoginUser = auth.principal as SimpleLoginUser
        var issuedAt : Date = Date()
        var notBefore : Date = Date(issuedAt.time)
        var expiresAt : Date = Date(issuedAt.time + EXPIRATION_TIME)
        var token : String = JWT.create()
            .withIssuedAt(issuedAt)
            .withNotBefore(notBefore)
            .withExpiresAt(expiresAt)
            .withSubject(simpleLoginUser.getUser().uuid)
            //add info
            .withClaim("X-NAME", simpleLoginUser.getUser().name)
            .withClaim("X-EMAIL",simpleLoginUser.getUser().email)
            .sign(SecurityConfig().jwtAlgorithm())
        logger.debug("generate token : {}", token)
        return token
    }

}