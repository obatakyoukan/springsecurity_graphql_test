package com.example.teamA.handler

import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.AuthorizationServiceException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.csrf.CsrfException
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Slf4j
class SimpleAccesssDeniedHandler : AccessDeniedHandler {
    constructor(){}

    @Throws(IOException::class)
    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {
        if( response!!.isCommitted() ){
            //
            return
        }
        dump(accessDeniedException!!)
        response.sendError(HttpStatus.FORBIDDEN.value(),HttpStatus.FORBIDDEN.reasonPhrase)
    }

    private fun dump(exception: AccessDeniedException){
        if( exception is AuthorizationServiceException){

        }else if( exception is CsrfException) {

        }else if( exception is org.springframework.security.web.server.csrf.CsrfException ){

        }else{

        }
    }

}