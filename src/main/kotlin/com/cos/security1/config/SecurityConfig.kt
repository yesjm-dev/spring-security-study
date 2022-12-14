package com.cos.security1.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록됨
class SecurityConfig {

    // @Bean: 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다
    @Bean
    fun encodePwd(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .authorizeRequests { authz ->
                authz
                    .antMatchers("/user/**").authenticated()
                    .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                    .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                    .anyRequest().permitAll()
                    .and()
                    .formLogin()
                    .loginPage("/loginForm")
//                    .usernameParameter("userNickname") -> username 으로 파라미터 안받으려면 설정 필요
                    .loginProcessingUrl("/login") // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인 진행해줌 -> login controller 가 필요없어짐
                    .defaultSuccessUrl("/")
            }
            .httpBasic(withDefaults())
            .cors().and()
            .csrf().disable()

        return http.build()
    }

}
