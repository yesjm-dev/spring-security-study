package com.cos.security1.controller

import com.cos.security1.model.User
import com.cos.security1.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller // view를 리턴하겠다
class IndexController(
    private val userRepository: UserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) {

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
}
