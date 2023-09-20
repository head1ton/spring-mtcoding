package ai.springmtcoding.config.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ai.springmtcoding.config.dummy.DummyObject;
import ai.springmtcoding.domain.user.User;
import ai.springmtcoding.domain.user.UserRepository;
import ai.springmtcoding.dto.user.UserReqDto.LoginReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@Transactional
class JwtAuthenticationFilterTest extends DummyObject {

    @Autowired
    private ObjectMapper om;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(newUser("test", "테스터"));
    }

    @Test
    @DisplayName("successfulAuthentication_test")
    public void successfulAuthentication_test() throws Exception {
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setUsername("test");
        loginReqDto.setPassword("1234");

        String requestBody = om.writeValueAsString(loginReqDto);
        System.out.println("requestBody = " + requestBody);

        ResultActions resultActions = mvc.perform(
            post("/api/login")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);
        System.out.println("responseBody = " + responseBody);

        String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtVO.HEADER);
        System.out.println("jwtToken = " + jwtToken);

        resultActions.andExpect(status().isOk());
        assertNotNull(jwtToken);
        assertTrue(jwtToken.startsWith(JwtVO.TOKEN_PRIFIX));
        resultActions.andExpect(jsonPath("$.data.username").value("test"));
    }

    @Test
    @DisplayName("unsuccessfulAuthentication_test")
    public void unsuccessfulAuthentication_test() throws Exception {
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setUsername("test");
        loginReqDto.setPassword("12345");   // 비밀번호 틀리게

        String requestBody = om.writeValueAsString(loginReqDto);
        System.out.println("requestBody = " + requestBody);

        ResultActions resultActions = mvc.perform(
            post("/api/login")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse()
                                           .getContentAsString(StandardCharsets.UTF_8);
        System.out.println("responseBody = " + responseBody);

        String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtVO.HEADER);
        System.out.println("jwtToken = " + jwtToken);

        resultActions.andExpect(status().isUnauthorized());
    }

}