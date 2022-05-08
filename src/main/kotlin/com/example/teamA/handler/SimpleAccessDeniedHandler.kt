package com.example.teamA.handler

import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.AuthorizationServiceException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.csrf.CsrfException
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.math.log

@Slf4j
class SimpleAccesssDeniedHandler : AccessDeniedHandler {
    constructor(){}
    private val logger : Logger
        = LoggerFactory.getLogger(SimpleAccesssDeniedHandler::class.java)

    @Throws(IOException::class)
    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {
        if( response!!.isCommitted() ){
            logger.info("Response has already been commited")
            return
        }
        dump(accessDeniedException!!)
        response.sendError(HttpStatus.FORBIDDEN.value(),HttpStatus.FORBIDDEN.reasonPhrase)
    }

    private fun dump(exception: AccessDeniedException){
        if( exception is AuthorizationServiceException){
            logger.debug("AuthorizationServiceException : {}", exception.message )
        }else if( exception is CsrfException) {
            logger.debug("CsrfException : {}", exception.message)
        }else if( exception is org.springframework.security.web.server.csrf.CsrfException ){
            logger.debug("security.web.server.csrf.CsrfException : {}", exception.message)
        }else{
            logger.debug("AccessDeniedException : {}", exception.message)
        }
    }

}