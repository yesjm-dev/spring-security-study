package com.cos.security1.config.oauth

import com.cos.security1.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User


// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인 진행
// 로그인 진행이 완료되면 시큐리티 session 을 만들어줌
// 세션 공간은 같지만 시큐리티만의 session 공간을 가짐 - (Security ContextHolder 키값을 담아 세션 정보를 저장함)
// 오브젝트는 정해져 있음 -> Authentication 타입 객체
// Authentication 안에 User 정보가 있어야 함
// User 오브젝트타입 -> UserDetails 타입 객체

// Security Session => Authentication => UserDetails(PrincipalDetails)

class PrincipalDetails(
    private val user: User, // 콤포지션
    private val attributes:  MutableMap<String, Any>? = null
) : UserDetails, OAuth2User {

    fun getUser() = user

    // 해당 User 의 권한을 리턴하는 곳
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities: MutableList<GrantedAuthority> = ArrayList()
        authorities.add(SimpleGrantedAuthority(user.role))
        return authorities
    }

    override fun getPassword(): String {
        return user.password.toString()
    }

    override fun getUsername(): String {
        return user.username.toString()
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        // 우리 사이트에서 1년동안 회원이 로그인을 안하면 휴먼 계정 설정
        // 현재시간 - 마지막 로그인 시간 -> 1년을 초과하면 return false 등의 설정 가능
        return true
    }

    override fun getName(): String? {
        return null
    }

    override fun getAttributes(): MutableMap<String, Any>? {
        return attributes
    }
}