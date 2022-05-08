package com.example.teamA.graphql

import com.example.teamA.entity.ResponseMessage
import com.example.teamA.entity.ResponseUser
import com.example.teamA.entity.ResponseUserList
import com.example.teamA.service.UserService
import graphql.kickstart.tools.GraphQLQueryResolver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class UserQueryResolver : GraphQLQueryResolver{
    @Autowired
    private lateinit var userService: UserService

    private val logger : Logger
        = LoggerFactory.getLogger(UserQueryResolver::class.java)


    fun test() : ResponseMessage{
        println(ResponseUserList("ok",userService.findAll()))
        return ResponseMessage("ok")
    }

    fun getUserByEmail(email : String) :
           // ResponseMessage
            ResponseUser
    {
        logger.info("start getUserByEmail")
        logger.info(email)
        var user = userService.findByEmail(email)
        println(user)
        return ResponseUser("ok", user)
    }

    @PreAuthorize("isAuthenticated()")
    //@Throws(org.springframework.security.access.AccessDeniedException)
    fun getUserList() : ResponseUserList {
        logger.info("get user list")
        var userList = userService.findAll()
        return ResponseUserList("ok", userList)
    }

}