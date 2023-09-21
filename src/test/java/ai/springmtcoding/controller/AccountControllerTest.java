package ai.springmtcoding.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ai.springmtcoding.config.dummy.DummyObject;
import ai.springmtcoding.domain.account.AccountRepository;
import ai.springmtcoding.domain.user.User;
import ai.springmtcoding.domain.user.UserRepository;
import ai.springmtcoding.dto.account.AccountReqDto.AccountSaveReqDto;
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
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@Sql("classpath:db/teardown.sql")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class AccountControllerTest extends DummyObject {

    @Autowired
    private ObjectMapper om;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private EntityManager em;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        dataSetting();
        em.clear();
    }

    private void dataSetting() {
        User test = userRepository.save(newUser("test", "테스터"));


    }

    // username=test 를 조회하고 세션에 담아주는 어노테이션
    // setupBefore=TEST_METHOD (setUp 메서드 실행 전에 수행)
    // setupBefore=TEST_EXECUTION (saveAccount_test 메서드 실행 전에 수행)
    @WithUserDetails(value = "test", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("saveAccount_test")
    public void saveAccount_test() throws Exception {
        AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
        accountSaveReqDto.setNumber(9999L);
        accountSaveReqDto.setPassword(1234L);

        String requestBody = om.writeValueAsString(accountSaveReqDto);
        System.out.println("requestBody = " + requestBody);

        ResultActions resultActions = mvc.perform(
            post("/api/s/account")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        resultActions.andExpect(status().isCreated());
    }
}