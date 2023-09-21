package ai.springmtcoding.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ai.springmtcoding.config.dummy.DummyObject;
import ai.springmtcoding.domain.user.UserRepository;
import ai.springmtcoding.dto.user.UserReqDto.JoinReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ActiveProfiles("test")
@Sql("classpath:db/teardown.sql")
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
class UserControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        userRepository.save(newUser("test", "테스트"));
        em.clear();
    }

    @Test
    @DisplayName("join_test")
    public void join_test() throws Exception {
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("love");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("love@gmail.com");
        joinReqDto.setFullname("러브");

        String requestBody = om.writeValueAsString(joinReqDto);
        System.out.println("requestBody = " + requestBody);

        ResultActions resultActions = mvc.perform(
            post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON)
        );
//        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
//        System.out.println("responseBody = " + responseBody);

        resultActions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("join_fail_test")
    public void join_fail_test() throws Exception {
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("test");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("test@gmail.com");
        joinReqDto.setFullname("테스트");

        String requestBody = om.writeValueAsString(joinReqDto);
        System.out.println("requestBody = " + requestBody);

        ResultActions resultActions = mvc.perform(
            post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        resultActions.andExpect(status().isBadRequest());
    }
}