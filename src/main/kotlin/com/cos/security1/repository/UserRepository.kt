package com.cos.security1.repository

import com.cos.security1.model.User
import org.springframework.data.jpa.repository.JpaRepository

// @Repository annotation 없어 IoC됨. JpaRepository 상속했기 때문
interface  UserRepository : JpaRepository<User, Int> {
    fun findByUsername(username: String): User?
}