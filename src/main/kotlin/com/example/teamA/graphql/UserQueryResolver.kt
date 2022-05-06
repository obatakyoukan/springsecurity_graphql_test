package com.example.teamA.graphql

import com.example.teamA.entity.ResponseMessage
import com.example.teamA.service.UserService
import graphql.kickstart.tools.GraphQLQueryResolver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserQueryResolver : GraphQLQueryResolver{
    @Autowired
    private lateinit var userService: UserService

    private val logger : Logger
        = LoggerFactory.getLogger(UserQueryResolver::class.java)

    fun test() : ResponseMessage {
        logger.info("test")
        return ResponseMessage("ok")
    }

}