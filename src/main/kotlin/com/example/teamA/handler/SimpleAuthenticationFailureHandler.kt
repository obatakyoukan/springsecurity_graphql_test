package com.example.teamA.handler

import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Slf4j
class SimpleAuthenticationFailureHandler : AuthenticationFailureHandler {
    constructor(){}
    private val logger : Logger
        = LoggerFactory.getLogger(SimpleAuthenticationFailureHandler::class.java)

    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?
    ) {
        if( response!!.isCommitted ){
            logger.info("Response has already been commited")
            return
        }
        response.sendError(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.reasonPhrase);
    }

}