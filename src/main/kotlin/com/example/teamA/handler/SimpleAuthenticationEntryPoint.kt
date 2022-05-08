package com.example.teamA.handler

import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Slf4j
class SimpleAuthenticationEntrypoint : AuthenticationEntryPoint {
    constructor(){}

    @Throws(IOException::class)
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        if(response!!.isCommitted){
            return
        }
        dump(authException!!)
        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.reasonPhrase);

    }

    private fun dump( e: AuthenticationException){
    }

}