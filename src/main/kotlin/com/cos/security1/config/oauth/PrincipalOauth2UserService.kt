package com.cos.security1.config.oauth

import com.cos.security1.model.User
import com.cos.security1.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class PrincipalOauth2UserService(
    private val userRepository: UserRepository
) : DefaultOAuth2UserService() {

    @Autowired
    private fun encodePwd(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        println("userRequest: $userRequest")
        println("clientRegistration: ${userRequest?.clientRegistration}") // registrationId로 어떤 OAuth로 로그인 했는지 확인
        println("accessToken: ${userRequest?.accessToken?.tokenValue}")

        // 구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인 완료 -> code를 리턴(OAtuh-Client라이브러리) -> AccessToken 요청
        // userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원 프로필을 받아준다
        val oauth2User: OAuth2User = super.loadUser(userRequest)
        println("attributes: ${oauth2User.attributes}")

        val provider = userRequest?.clientRegistration?.clientId // google
        val providerId = oauth2User.getAttribute<String>("sub")
        val username = provider + "_" + providerId // google_1000000000000000
        val password = encodePwd().encode("겟인데어")
        val email = oauth2User.getAttribute<String>("email")
        val role = "ROLE_USER"

        var userEntity = userRepository.findByUsername(username)

        if (userEntity == null) {
            userEntity = User(username = username, password = password, email = email, role = role, provider = provider, providerId = providerId)
            userRepository.save(userEntity)
        } else {
            println("구글 로그인을 이미 한적이 있습니다. 당신은 자동회원가입이 되어 있습니다.")
        }

        return PrincipalDetails(userEntity, oauth2User.attributes)
    }
}