package com.example.teamA.controller

import com.example.teamA.entity.ResponseUserList
import com.example.teamA.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {
    @Autowired
    private lateinit var userService: UserService

    @GetMapping("/list")
    fun getlist() : ResponseUserList {
        return ResponseUserList("ok", userService.findAll() )
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/seclist")
    fun getTestList() : ResponseUserList {
        return ResponseUserList("ok", userService.findAll())
    }

}