package com.example.teamA.graphql

import com.example.teamA.entity.ResponseMessage
import com.example.teamA.entity.User
import com.example.teamA.service.UserService
import graphql.kickstart.tools.GraphQLMutationResolver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserMutationResolver : GraphQLMutationResolver {
    @Autowired
    private lateinit var userService: UserService

    private val logger : Logger
        = LoggerFactory.getLogger(UserMutationResolver::class.java)

    fun signup(name : String, email : String, password : String ): ResponseMessage? {
        logger.info(email)
        userService.createUser(name,email,password)
        return ResponseMessage("ok")
    }
}