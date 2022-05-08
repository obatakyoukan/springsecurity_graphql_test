package com.example.teamA.handler

import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.session.SessionAuthenticationException
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Slf4j
class SimpleAuthenticationEntrypoint : AuthenticationEntryPoint {
    constructor(){}
    private val logger : Logger
        = LoggerFactory.getLogger(SimpleAuthenticationEntrypoint::class.java)

    @Throws(IOException::class)
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        if(response!!.isCommitted){
            logger.info("Response has already been commited")
            return
        }
        dump(authException!!)
        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.reasonPhrase);

    }

    private fun dump( e: AuthenticationException){
        if( e is BadCredentialsException ){
            logger.debug("BadCredentialException : {}" , e.message)
        }else if(e is LockedException){
            logger.debug("LockedException : {}",  e.message)
        }else if(e is DisabledException ){
            logger.debug("DisabledException : {}", e.message)
        }else if(e is AccountExpiredException){
            logger.debug("AccountExpiredException : {}", e.message)
        }else if(e is CredentialsExpiredException){
            logger.debug("CredentialsExpiredException : {}", e.message)
        }else if(e is SessionAuthenticationException){
            logger.debug("SessionAuthenticationException : {}", e.message)
        }else{
            logger.debug("AuthenticationException : {}", e.message)
        }
    }

}