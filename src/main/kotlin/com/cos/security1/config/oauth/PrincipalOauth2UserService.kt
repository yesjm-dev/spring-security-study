package com.cos.security1.config.oauth

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class PrincipalOauth2UserService : DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        println("userRequest: $userRequest")
        println("clientRegistration: ${userRequest?.clientRegistration}") // registrationId로 어떤 OAuth로 로그인 했는지 확인
        println("accessToken: ${userRequest?.accessToken?.tokenValue}")

        // 구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인 완료 -> code를 리턴(OAtuh-Client라이브러리) -> AccessToken 요청
        // userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원 프로필을 받아준다
        println("attributes: ${super.loadUser(userRequest).attributes}")

        val oauth2User: OAuth2User = super.loadUser(userRequest)

        return super.loadUser(userRequest)
    }
}