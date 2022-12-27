package com.cos.security1.config

import com.cos.security1.config.oauth.PrincipalOauth2UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize, postAuthorize 어노테이션 활성화
class SecurityConfig {

    @Autowired lateinit var principalOauth2UserService: PrincipalOauth2UserService

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
                    .and()
                    .oauth2Login()
                    .loginPage("/loginForm")
                    .userInfoEndpoint()
                    .userService(principalOauth2UserService) // 구글 로그인이 완료된 뒤의 후처리가 필요함. Tip. 코드X (액세스토큰+사용자프로필정보)
                /**
                 * 1. 코드받기(인증)
                 * 2. 액세스토큰(권한)
                 * 3. 사용자 프로필 정보를 가져오기
                 * 4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함
                 * 4-2. (이메일, 전화번호, 이름, 아이디) 외에 쇼핑몰 -> 집주소, 백화점몰 -> (vip등급, 일반등급) 등의 추가 정보가 필요함
                 *
                 */
            }
            .httpBasic(withDefaults())
            .cors().and()
            .csrf().disable()

        return http.build()
    }

}
