package ai.springmtcoding.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ai.springmtcoding.config.dummy.DummyObject;
import ai.springmtcoding.domain.account.Account;
import ai.springmtcoding.domain.account.AccountRepository;
import ai.springmtcoding.domain.user.User;
import ai.springmtcoding.domain.user.UserRepository;
import ai.springmtcoding.dto.account.AccountReqDto.AccountSaveReqDto;
import ai.springmtcoding.dto.account.AccountRespDto.AccountSaveRespDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest extends DummyObject {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Spy
    private ObjectMapper om;

    @Test
    @DisplayName("account_create_test")
    public void account_create_test() throws Exception {

        Long userId = 1L;

        AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
        accountSaveReqDto.setNumber(1111L);
        accountSaveReqDto.setPassword(1234L);

        User test = newMockUser(userId, "test", "테스터");
        when(userRepository.findById(any())).thenReturn(Optional.of(test));

        when(accountRepository.findByNumber(any())).thenReturn(Optional.empty());

        Account testAccount = newMockAccount(1L, 1111L, 1000L, test);
        when(accountRepository.save(any())).thenReturn(testAccount);

        AccountSaveRespDto accountSaveRespDto = accountService.accountCreate(accountSaveReqDto,
            userId);
        String responseBody = om.writeValueAsString(accountSaveRespDto);
        System.out.println("responseBody = " + responseBody);

        assertThat(accountSaveRespDto.getNumber()).isEqualTo(1111L);
    }

}