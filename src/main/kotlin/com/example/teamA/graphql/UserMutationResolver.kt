package com.example.teamA.graphql

import com.example.teamA.entity.ResponseMessage
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
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component


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

    //@PreAuthorize("isAnonymous()")
    fun login(email: String, password: String) : ResponseMessage? {
        val credentials = UsernamePasswordAuthenticationToken(email, password)
        try {
            SecurityContextHolder.getContext().authentication = authenticationProvider.authenticate(credentials)
            simpleUserDetailService.getCurrentUser()
            logger.info("end login")
            return ResponseMessage("ok")
        } catch (ex: AuthenticationException) {
            throw BadCredentialsException(email)
        }
    }

}