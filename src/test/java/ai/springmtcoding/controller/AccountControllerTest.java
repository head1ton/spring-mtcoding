package ai.springmtcoding.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ai.springmtcoding.config.dummy.DummyObject;
import ai.springmtcoding.domain.account.Account;
import ai.springmtcoding.domain.account.AccountRepository;
import ai.springmtcoding.domain.user.User;
import ai.springmtcoding.domain.user.UserRepository;
import ai.springmtcoding.dto.account.AccountReqDto.AccountDepositReqDto;
import ai.springmtcoding.dto.account.AccountReqDto.AccountSaveReqDto;
import ai.springmtcoding.dto.account.AccountReqDto.AccountTransferReqDto;
import ai.springmtcoding.dto.account.AccountReqDto.AccountWithdrawReqDto;
import ai.springmtcoding.handler.ex.CustomApiException;
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
        User cos = userRepository.save(newUser("cos", "코스"));

        Account testAccount1 = accountRepository.save(newAccount(1111L, test));
        Account cosAccount = accountRepository.save(newAccount(2222L, cos));


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

    @WithUserDetails(value = "test", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("deleteAccount_test")
    public void deleteAccount_test() throws Exception {
        Long number = 1111L;

        ResultActions resultActions = mvc.perform(
            delete("/api/s/account/" + number)
        );

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        assertThrows(CustomApiException.class,
            () -> accountRepository.findByNumber(number).orElseThrow(
                () -> new CustomApiException("계좌를 찾을 수 없습니다.")
            ));
    }

    @Test
    @DisplayName("depositAccount_test")
    public void depositAccount_test() throws Exception {
        AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
        accountDepositReqDto.setNumber(1111L);
        accountDepositReqDto.setAmount(100L);
        accountDepositReqDto.setGubun("DEPOSIT");
        accountDepositReqDto.setTel("01088887777");

        String requestBody = om.writeValueAsString(accountDepositReqDto);
        System.out.println("requestBody = " + requestBody);

        ResultActions resultActions = mvc.perform(
            post("/api/account/deposit")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        resultActions.andExpect(status().isCreated());
    }

    @WithUserDetails(value = "test", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("withdrawAccount_test")
    public void withdrawAccount_test() throws Exception {
        AccountWithdrawReqDto accountWithdrawReqDto = new AccountWithdrawReqDto();
        accountWithdrawReqDto.setNumber(1111L);
        accountWithdrawReqDto.setPassword(1234L);
        accountWithdrawReqDto.setAmount(100L);
        accountWithdrawReqDto.setGubun("WITHDRAW");

        String requestBody = om.writeValueAsString(accountWithdrawReqDto);
        System.out.println("requestBody = " + requestBody);

        ResultActions resultActions = mvc.perform(
            post("/api/s/account/withdraw")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        resultActions.andExpect(status().isCreated());
    }

    @WithUserDetails(value = "test", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    @DisplayName("transferAccount_test")
    public void transferAccount_test() throws Exception {
        AccountTransferReqDto accountTransferReqDto = new AccountTransferReqDto();
        accountTransferReqDto.setWithdrawNumber(1111L);
        accountTransferReqDto.setDepositNumber(2222L);
        accountTransferReqDto.setWithdrawPassword(1234L);
        accountTransferReqDto.setAmount(100L);
        accountTransferReqDto.setGubun("TRANSFER");

        String requestBody = om.writeValueAsString(accountTransferReqDto);
        System.out.println("requestBody = " + requestBody);

        ResultActions resultActions = mvc.perform(
            post("/api/s/account/transfer")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        resultActions.andExpect(status().isCreated());
    }
}