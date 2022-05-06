package com.example.teamA.service

import com.example.teamA.entity.User
import com.example.teamA.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.Date

@Service
class UserService {
    @Autowired
    private lateinit var userRepository: UserRepository

    private val logger : Logger = LoggerFactory.getLogger(UserService::class.java)
    private val passwordEncoder : PasswordEncoder
            = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    fun findAll() = userRepository.findAll()
    fun findByEmail(email: String) = userRepository.findByEmail(email)

    fun createUser(name : String, email : String, password : String){
        logger.info("create User")
        val date = Date()
        val hash = passwordEncoder.encode(password)
        var user = User(
            name=name,
            email=email,
            password=hash,
            created_at = date,
            updated_at = date
        )
        userRepository.save(user)
    }

    fun updateUser(name: String?, email: String, password: String?){
        logger.info("update User")
        var user = userRepository.findByEmail(email)
        if( user == null ){
            logger.error("user is null.")
            return
        }
        if( name != null ) {
            user.name = name
        }
        if( password != null ){
            val hash = passwordEncoder.encode(password)
        }
        val date = Date()
        user.updated_at = date
        userRepository.save(user)
    }

    fun deleteUser(email: String){
        logger.info("delete User")
        var user = userRepository.findByEmail(email)
        if( user == null ){
            logger.error("user is null")
            return
        }
        userRepository.delete(user)
    }

}