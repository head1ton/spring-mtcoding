package ai.springmtcoding.config;

import ai.springmtcoding.config.jwt.JwtAuthenticationFilter;
import ai.springmtcoding.config.jwt.JwtAuthorizationFilter;
import ai.springmtcoding.domain.user.UserEnum;
import ai.springmtcoding.dto.ResponseDto;
import ai.springmtcoding.util.CustomResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        log.debug("BCryptPasswordEncoder 빈 등록");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("filterChain 빈 등록");
        http.headers().frameOptions().sameOrigin(); //iframe 허용 안함
        http.csrf().disable(); //// enable 이면 postman 작동 안함
        http.cors().configurationSource(configurationSource());

        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);    // jSessionId를 서버쪽에서 관리 안함
        http.formLogin().disable(); // 기본 로그인 창 사용 안함
        http.httpBasic().disable(); // httpBasic은 브라우저가 팝업창을 이용해서 사용자 인증

        // 필터 적용
        http.apply(new CustomSecurityFilterManager());

        // 인증 실패
        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
            CustomResponseUtil.fail(response, "로그인을 진행해 주세요.", HttpStatus.UNAUTHORIZED);
        });

        // 권한 실패
        http.exceptionHandling().accessDeniedHandler((request, response, accessDeniedException) -> {
            CustomResponseUtil.fail(response, "권한이 없습니다.", HttpStatus.FORBIDDEN);
        });

        http.authorizeRequests()
            .antMatchers("/api/s/**").authenticated()
            .antMatchers("/api/admin/**").hasRole("" + UserEnum.ADMIN)
            .anyRequest().permitAll();

        return http.build();
    }

    // JWT 필터 등록이 필요함
    public class CustomSecurityFilterManager extends
        AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {

        @Override
        public void configure(final HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(
                AuthenticationManager.class);
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager));
            builder.addFilter(new JwtAuthorizationFilter(authenticationManager));
            super.configure(builder);
        }
    }

    public CorsConfigurationSource configurationSource() {
        log.debug("configurationSource cors 설정이 SecurityFilterChain에 등록됨");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");    // GET, POST, PUT, DELETE (javascript 요청 허용)
        configuration.addAllowedOriginPattern("*"); //  모든 IP 주소 허용
        configuration.setAllowCredentials(true);        //클라이언트에서 쿠키 요청 허용
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
