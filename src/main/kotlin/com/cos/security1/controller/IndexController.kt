package com.cos.security1.controller

import com.cos.security1.config.oauth.PrincipalDetails
import com.cos.security1.model.User
import com.cos.security1.repository.UserRepository
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller // view를 리턴하겠다
class IndexController(
    private val userRepository: UserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) {

    @GetMapping("/test/login")
    @ResponseBody
    fun loginTest(authentication: Authentication): String {
        println("/test/login =================")
        val principalDetails: PrincipalDetails = authentication.principal as PrincipalDetails
        println("authentication: ${principalDetails.getUser()}")

        return "세션 정보 확인하기"
    }

    @GetMapping("/test/oauth/login")
    @ResponseBody
    fun testOAuthLogin(authentication: Authentication): String {
        println("/test/oauth/login =================")
        val oauth2User: OAuth2User = authentication.principal as OAuth2User
        println("authentication: ${oauth2User.attributes}")

        return "OAuth 세션 정보 확인하기"
    }

    // localhost:8080
    @GetMapping("/")
    fun index(): String {
        // 머스테치 기본폴더 src/main/resources/
        // view resolver 설정: templates (prefix), .mustache (suffix) 생략가능
        return "index" // src/main/resources/templates/index.mustache
    }

    @ResponseBody
    @GetMapping("/user")
    fun user(): String {
        return "user"
    }

    @ResponseBody
    @GetMapping("/admin")
    fun admin(): String {
        return "admin"
    }

    @ResponseBody
    @GetMapping("/manager")
    fun manager(): String {
        return "manager"
    }

    @GetMapping("/loginForm")
    fun loginForm(): String {
        return "loginForm"
    }

    @PostMapping("/join")
    fun join(user: User): String {
        println("${user.username} / ${user.email}")
        user.role = "ROLE_USER"
        val rawPassword = user.password
        val encPassword = bCryptPasswordEncoder.encode(rawPassword)

        user.password = encPassword
        userRepository.save(user)
        return "redirect:/loginForm"
    }

    @GetMapping("/joinForm")
    fun joinForm(): String {
        return "joinForm"
    }

    @ResponseBody
    @GetMapping("/joinProc")
    fun joinProc(): String {
        return "회원가입 완료됨"
    }

    @Secured("ROLE_ADMIN") // 특정 api 에만 권한 설정을 하고 싶은 경우에 사용
    @ResponseBody
    @GetMapping("/info")
    fun info(): String {
        return "개인정보"
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 특정 api 에만 권한 설정을 하고 싶은 경우에 사용
    @ResponseBody
    @GetMapping("/data")
    fun data(): String {
        return "데이터정보"
    }
}
