package com.example.teamA.service

import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.teamA.config.SecurityConfig
import com.example.teamA.entity.SimpleLoginUser
import com.example.teamA.entity.User
import com.example.teamA.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import kotlin.math.log

@Service
class SimpleUserDetailService : UserDetailsService {
    private var logger : Logger
        = LoggerFactory.getLogger(SimpleUserDetailService::class.java)

    @Autowired
    private lateinit var verifier : JWTVerifier

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(email: String?): UserDetails? {
        logger.info("start loadUserByUsername")
        if( email == null ){
            logger.error("email is null in SimpleUserDetailService")
            return null
        }
        var user : User? = userRepository.findByEmail(email)
        if( user == null ){
            UsernameNotFoundException("user not found")
            return null
        }
        logger.info(user.toString())
        return SimpleLoginUser(user)
    }

    fun loadUserByToken(token : String) : SimpleLoginUser?
    {
        var decodedJWT = getDecodedToken(token)
        if( decodedJWT == null ){
            logger.error("decodeJWT is null")
            return null
        }
        logger.info(decodedJWT.subject)
        var user = userRepository.findByEmail(decodedJWT.subject)
        if( user == null ){
            logger.error("user not found in this email")
            return null
        }
        return SimpleLoginUser(user)
    }

    fun getDecodedToken(token: String) : DecodedJWT?{
        try{
            return verifier.verify(token)
        }catch (ex : JWTVerificationException){
            logger.error("getDecodedToken error")
            return null
        }
    }

    fun getCurrentUser() : User?{
        var context = SecurityContextHolder.getContext()
        return userRepository.findByEmail(
            context.authentication.name
        )
    }

}