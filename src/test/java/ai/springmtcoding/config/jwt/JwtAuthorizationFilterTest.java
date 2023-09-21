package ai.springmtcoding.config.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ai.springmtcoding.config.auth.LoginUser;
import ai.springmtcoding.domain.user.User;
import ai.springmtcoding.domain.user.UserEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class JwtAuthorizationFilterTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("authorization_success_test")
    public void authorization_success_test() throws Exception {
        User user = User.builder().id(1L).role(UserEnum.CUSTOM).build();
        LoginUser loginUser = new LoginUser(user);
        String jwtToken = JwtProcess.create(loginUser);
        System.out.println("jwtToken = " + jwtToken);

        ResultActions resultActions = mvc.perform(
            get("/api/s/hello/test")
                .header(JwtVO.HEADER, jwtToken)
        );

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("authorization_fail_test")
    public void authorization_fail_test() throws Exception {

        ResultActions resultActions = mvc.perform(
            get("/api/s/hello/test")
        );

        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("authorization_admin_test")
    public void authorization_admin_test() throws Exception {
        User user = User.builder().id(1L).role(UserEnum.CUSTOM).build();
        LoginUser loginUser = new LoginUser(user);
        String jwtToken = JwtProcess.create(loginUser);
        System.out.println("jwtToken = " + jwtToken);

        ResultActions resultActions = mvc.perform(
            get("/api/admin/hello/test")
                .header(JwtVO.HEADER, jwtToken)
        );

        resultActions.andExpect(status().isForbidden());
    }

}