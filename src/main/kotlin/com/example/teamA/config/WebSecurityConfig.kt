package com.example.teamA.config

import lombok.RequiredArgsConstructor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityProperties::class)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    private val logger: Logger
        = LoggerFactory.getLogger(WebSecurityConfig::class.java)

    override fun configure(http: HttpSecurity) {
        logger.info("configure http")
        http.authorizeRequests()
            .anyRequest().permitAll()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            //.addFilterBefore()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        logger.info("configure auth")
    }

}