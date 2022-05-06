package com.example.teamA.graphql

import com.example.teamA.entity.ResponseMessage
import com.example.teamA.service.UserService
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UserQueryResolver : GraphQLQueryResolver{
    @Autowired
    private lateinit var userService: UserService

    fun test() : ResponseMessage {
        return ResponseMessage("ok")
    }

}