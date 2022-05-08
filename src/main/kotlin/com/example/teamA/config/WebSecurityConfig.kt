package com.example.teamA.config

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.teamA.handler.SimpleAccesssDeniedHandler
import com.example.teamA.handler.SimpleAuthenticationEntrypoint
import com.example.teamA.handler.SimpleAuthenticationFailureHandler
import com.example.teamA.handler.SimpleAuthenticationSuccessHandler
import com.example.teamA.service.SimpleUserDetailService
import com.example.teamA.service.UserService
import lombok.RequiredArgsConstructor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter
import org.springframework.web.filter.GenericFilterBean

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityProperties::class)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    private val logger: Logger
        = LoggerFactory.getLogger(WebSecurityConfig::class.java)

    @Autowired
    private lateinit var simpleUserDetailService: SimpleUserDetailService
    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
        //= SecurityConfig().passwordEncoder()

    @Autowired
    private lateinit var authenticationProvider : AuthenticationProvider
        //= SecurityConfig().authenticationProvider(simpleUserDetailService,passwordEncoder)

    //@Autowired
    //private lateinit var jwtFilter: JWTFilter

    @Value("\${security.secret-key:secret}")
    private var secretKey : String = "secret"

    override fun configure(http: HttpSecurity) {
        logger.info("configure http")
        http.authorizeRequests()
            .anyRequest()
            //.authenticated()
            .permitAll()
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(SimpleAuthenticationEntrypoint())
            .accessDeniedHandler(SimpleAccesssDeniedHandler())
            .and()
            .formLogin()
            .loginProcessingUrl("/login").permitAll()
            .usernameParameter("email")
            .passwordParameter("password")
            .successHandler(authenticationsSuccessHandler())
            .failureHandler(SimpleAuthenticationFailureHandler())
            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessHandler(HttpStatusReturningLogoutSuccessHandler() )
            .and()
            .addFilterBefore(tokenFilter(), UsernamePasswordAuthenticationFilter::class.java )
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            //.addFilterBefore(jwtFilter,RequestHeaderAuthenticationFilter::class.java)
            .csrf().disable()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        logger.info("configure auth")
        auth.authenticationProvider(authenticationProvider)
    }


    fun authenticationsSuccessHandler() : AuthenticationSuccessHandler{
        return SimpleAuthenticationSuccessHandler()
    }

    fun tokenFilter() : GenericFilterBean{
        return SimpleTokenFilter(userService)
    }




}