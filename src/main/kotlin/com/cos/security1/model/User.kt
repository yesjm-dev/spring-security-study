package com.cos.security1.model

import org.hibernate.annotations.CreationTimestamp
import java.sql.Timestamp
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null
    var username: String? = null
    var password: String? = null
    var email: String? = null
    var role: String? = null
    var provider: String? = null
    var providerId: String? = null
    @CreationTimestamp
    val createDate: Timestamp? = null
}