package ai.springmtcoding.config.jwt;

import ai.springmtcoding.config.auth.LoginUser;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

// 토큰 검증 (모든 주소에서 동작)
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(
        final AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
        final HttpServletResponse response,
        final FilterChain chain) throws IOException, ServletException {

        if (isHeaderVerify(request, response)) {
            logger.debug("토큰이 존재함");

            String token = request.getHeader(JwtVO.HEADER).replace(JwtVO.TOKEN_PRIFIX, "");
            LoginUser loginUser = JwtProcess.verify(token);
            logger.debug("토큰이 검증 완료");

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginUser, null, loginUser.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("임시 세션이 생성됨");
        }
        chain.doFilter(request, response);
    }

    private boolean isHeaderVerify(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(JwtVO.HEADER);
        return header != null && header.startsWith(JwtVO.TOKEN_PRIFIX);
    }
}
