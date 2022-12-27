package com.cos.security1.config.oauth

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class PrincipalOauth2UserService : DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        println("userRequest: $userRequest")
        println("clientRegistration: ${userRequest?.clientRegistration}")
        println("accessToken: ${userRequest?.accessToken?.tokenValue}")
        println("attributes: ${super.loadUser(userRequest).attributes}")
        return super.loadUser(userRequest)
    }
}