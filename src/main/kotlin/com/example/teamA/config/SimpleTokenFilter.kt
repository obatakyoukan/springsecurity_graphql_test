package com.example.teamA.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import com.example.teamA.entity.SimpleLoginUser
import com.example.teamA.entity.User
import com.example.teamA.service.UserService
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import java.util.Optional
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Slf4j
class SimpleTokenFilter : GenericFilterBean {
    private var userService : UserService

    private var algorithm : Algorithm

    //private val logger : Logger
    //    = LoggerFactory.getLogger(SimpleTokenFilter::class.java)

    constructor(userService: UserService){
        this.algorithm = SecurityConfig().jwtAlgorithm()
        this.userService = userService
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?)
    {
        var token : String? = resolveToken(request)
        if( token == null ){
            logger.debug("token is null")
            chain!!.doFilter(request,response)
            return
        }

        try{
            authentication(verifyToken(token))
        }catch ( e : JWTVerificationException ){
            //log verify error
            SecurityContextHolder.clearContext()
            ( response as HttpServletResponse ).sendError(HttpStatus.UNAUTHORIZED.value(),HttpStatus.UNAUTHORIZED.reasonPhrase )
        }
        chain!!.doFilter(request,response)
    }

    private fun resolveToken(request : ServletRequest?) : String?
    {
        if( request == null){
            logger.info("request is null in func resolveToken")
            return null
        }

        var token : String? =
            (request as HttpServletRequest)
                .getHeader("Authorization")

        logger.info("get token")
        logger.info(token)

        if( token == null || !token.startsWith("Bearer ")){
            return null
        }
        return token.substring(7)
    }

    private fun verifyToken(token : String) : DecodedJWT{
        var verifier : JWTVerifier = JWT.require(algorithm).build()
        return verifier.verify(token)
    }

    private fun authentication(jwt : DecodedJWT){
        logger.info("jwt subject value")
        logger.info(jwt.subject)
        var userId : String? = jwt.subject
        logger.info("user_Id")
        logger.info(userId)
        if( userId == null ){
            logger.error("userId is null")
            logger.error("subject is " + jwt.subject )
            return
        }

        var user : Optional<User> = userService.findById(userId)
        if( !user.isPresent() ){
            return
        }
        var simpleLoginUser = SimpleLoginUser(user.get())
        SecurityContextHolder.getContext()
            .authentication = UsernamePasswordAuthenticationToken(
            simpleLoginUser,
            null,
            simpleLoginUser.authorities
        )

    }

}