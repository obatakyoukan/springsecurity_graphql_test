package com.example.teamA.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils

class SimpleLoginUser
    : org.springframework.security.core.userdetails.User
{
    private var user : User
    fun getUser() : User {
        return user
    }
    constructor(user: User)
        : super(user.name,user.password, determineRole(false))
    {
            this.user = user
    }

}
private val USER_ROLES : List<GrantedAuthority> =
    AuthorityUtils.createAuthorityList("ROLE_USER")
private val ADMIN_ROLES : List<GrantedAuthority> =
    AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN")

private fun determineRole(isAdmin: Boolean) : List<GrantedAuthority>{
    //return isAdmin ? ADMIN_ROLES : USER_ROLES
    if( isAdmin ) return ADMIN_ROLES
    return USER_ROLES
}