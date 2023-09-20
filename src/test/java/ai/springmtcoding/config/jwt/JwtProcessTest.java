package ai.springmtcoding.config.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import ai.springmtcoding.config.auth.LoginUser;
import ai.springmtcoding.domain.user.User;
import ai.springmtcoding.domain.user.UserEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtProcessTest {

    private String createToken() {
        User user = User.builder().id(1L).role(UserEnum.CUSTOM).build();
        LoginUser loginUser = new LoginUser(user);

        return JwtProcess.create(loginUser);
    }

    @Test
    @DisplayName("create_test")
    public void create_test() throws Exception {

        String jwtToken = createToken();
        System.out.println("jwtToken = " + jwtToken);

        assertTrue(jwtToken.startsWith(JwtVO.TOKEN_PRIFIX));
    }

    @Test
    @DisplayName("verify_test")
    public void verify_test() throws Exception {

        String token = createToken();
        String jwtToken = token.replace(JwtVO.TOKEN_PRIFIX, "");
        System.out.println("jwtToken = " + jwtToken);

        LoginUser loginUser = JwtProcess.verify(jwtToken);
        System.out.println("loginUser = " + loginUser.getUser().getId());
        System.out.println("loginUser = " + loginUser.getUser().getRole().name());

        assertThat(loginUser.getUser().getId()).isEqualTo(1L);
        assertThat(loginUser.getUser().getRole()).isEqualTo(UserEnum.CUSTOM);
    }
}