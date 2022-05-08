package com.example.teamA.handler

import lombok.extern.slf4j.Slf4j
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.teamA.config.SecurityConfig
import com.example.teamA.entity.SimpleLoginUser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.web.WebAttributes
import javax.servlet.http.HttpSession

@Slf4j
class SimpleAuthenticationSuccessHandler : AuthenticationSuccessHandler {
    private var algorithm : Algorithm
    private val logger : Logger
        = LoggerFactory.getLogger(SimpleAuthenticationSuccessHandler::class.java)
    constructor(){
        this.algorithm = SecurityConfig().jwtAlgorithm()
    }

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        if( response!!.isCommitted() ){
            logger.info("Response has already committed.")
            return
        }
        setToken(response,generateToken(authentication!!))
        response.status = HttpStatus.OK.value()
        clearAuthenticationAttributes(request!!)
    }

    private val EXPIRATION_TIME : Long = TimeUnit.MINUTES.toMillis(10L)

    private fun generateToken( auth : Authentication ) : String {
        var simpleLoginUser : SimpleLoginUser = auth.principal as SimpleLoginUser
        var issuedAt : Date = Date()
        var notBefore : Date = Date(issuedAt.time)
        var expiresAt : Date = Date(issuedAt.time + EXPIRATION_TIME)
        var token : String = JWT.create()
            .withIssuedAt(issuedAt)
            .withNotBefore(notBefore)
            .withExpiresAt(expiresAt)
            .withSubject(simpleLoginUser.getUser().uuid)
            //add info
            .withClaim("X-NAME", simpleLoginUser.getUser().name)
            .withClaim("X-EMAIL",simpleLoginUser.getUser().email)
            .sign(this.algorithm)
        logger.debug("generate token : {}", token)
        return token
    }

    private fun setToken(
        response : HttpServletResponse,
        token : String
    ){
        response.setHeader("Authorization" , "Bearer %s".format(token) )
    }

    private fun clearAuthenticationAttributes(
        request: HttpServletRequest
    ){
        var session : HttpSession? = request.getSession(false)
        if( session == null ){
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)
    }



}