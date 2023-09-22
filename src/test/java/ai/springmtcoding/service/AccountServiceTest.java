package ai.springmtcoding.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ai.springmtcoding.config.dummy.DummyObject;
import ai.springmtcoding.domain.account.Account;
import ai.springmtcoding.domain.account.AccountRepository;
import ai.springmtcoding.domain.transaction.Transaction;
import ai.springmtcoding.domain.transaction.TransactionRepository;
import ai.springmtcoding.domain.user.User;
import ai.springmtcoding.domain.user.UserRepository;
import ai.springmtcoding.dto.account.AccountReqDto.AccountDepositReqDto;
import ai.springmtcoding.dto.account.AccountReqDto.AccountSaveReqDto;
import ai.springmtcoding.dto.account.AccountReqDto.AccountTransferReqDto;
import ai.springmtcoding.dto.account.AccountReqDto.AccountWithdrawReqDto;
import ai.springmtcoding.dto.account.AccountRespDto.AccountDepositRespDto;
import ai.springmtcoding.dto.account.AccountRespDto.AccountSaveRespDto;
import ai.springmtcoding.handler.ex.CustomApiException;
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

    @Mock
    private TransactionRepository transactionRepository;

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

    @Test
    @DisplayName("account_delete_test")
    public void account_delete_test() throws Exception {
        Long number = 1111L;
        Long userId = 2L;

        User test = newMockUser(1L, "test", "테스터");
        Account testAccount = newMockAccount(userId, number, 1000L, test);

        when(accountRepository.findByNumber(any())).thenReturn(Optional.of(testAccount));

        assertThrows(CustomApiException.class, () -> accountService.accountDelete(number, userId));
    }

    @Test
    @DisplayName("accountDeposit_test")
    public void accountDeposit_test() throws Exception {
        // given
        AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
        accountDepositReqDto.setNumber(1111L);
        accountDepositReqDto.setAmount(100L);
        accountDepositReqDto.setGubun("DEPOSIT");
        accountDepositReqDto.setTel("01088887777");

        // stub 1L
        User test = newMockUser(1L, "test", "테스터"); // 실행됨
        Account testAccount1 = newMockAccount(1L, 1111L, 1000L,
            test); // 실행됨 - testAccount1 -> 1000원
        when(accountRepository.findByNumber(any())).thenReturn(
            Optional.of(testAccount1)); // 실행안됨 -> service호출후 실행됨 ->
        // 1100원

        // stub 2 (스텁이 진행될 때 마다 연관된 객체는 새로 만들어서 주입하기 - 타이밍 때문에 꼬인다)
        Account testAccount2 = newMockAccount(1L, 1111L, 1000L,
            test); // 실행됨 - testAccount1 -> 1000원
        Transaction transaction = newMockDepositTransaction(1L, testAccount2);
        when(transactionRepository.save(any())).thenReturn(transaction); // 실행안됨

        // when
        AccountDepositRespDto accountDepositRespDto = accountService.accountDeposit(
            accountDepositReqDto);
        System.out.println("테스트 : 트랜잭션 입금계좌 잔액 : " + accountDepositRespDto.getTransaction()
                                                                          .getDepositAccountBalance());
        System.out.println("테스트 : 계좌쪽 잔액 : " + testAccount1.getBalance());
        System.out.println("테스트 : 계좌쪽 잔액 : " + testAccount2.getBalance());

        // then
        assertThat(testAccount1.getBalance()).isEqualTo(1100L);
        assertThat(accountDepositRespDto.getTransaction().getDepositAccountBalance()).isEqualTo(
            1100L);

    }

    // 계좌출금 서비스
    @Test
    @DisplayName("accountWithdraw_test")
    public void accountWithdraw_test() throws Exception {
        Long amount = 100L;
        Long password = 1234L;
        Long userId = 1L;

        User test = newMockUser(1L, "test", "테스터");
        Account testAccount = newMockAccount(1L, 1111L, 1000L, test);

        if (amount <= 0L) {
            throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
        }

        testAccount.checkOwner(userId);
        testAccount.checkSamePassword(password);
//        testAccount.checkBalance(amount);
        testAccount.withdraw(amount);

        assertThat(testAccount.getBalance()).isEqualTo(900L);
    }

    @Test
    @DisplayName("accountTransfer_test")
    public void accountTransfer_test() throws Exception {
        Long userId = 1L;

        AccountTransferReqDto accountTransferReqDto = new AccountTransferReqDto();
        accountTransferReqDto.setWithdrawNumber(1111L);
        accountTransferReqDto.setDepositNumber(2222L);
        accountTransferReqDto.setWithdrawPassword(1234L);
        accountTransferReqDto.setAmount(100L);
        accountTransferReqDto.setGubun("TRANSFER");

        User test = newMockUser(1000L, "test", "테스터");
        User cos = newMockUser(2000L, "cos", "코스");
        Account withdrawAccount = newMockAccount(1L, 1111L, 1000L, test);
        Account depositAccount = newMockAccount(2L, 2222L, 1000L, cos);

        if (accountTransferReqDto.getWithdrawNumber().longValue()
            == accountTransferReqDto.getDepositNumber().longValue()) {
            throw new CustomApiException("입출금 계좌가 동일할 수 없습니다.");
        }

        if (accountTransferReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
        }

        // 출금 소유자 확인
        withdrawAccount.checkBalance(userId);

        // 출금 계좌 비밀번호
        withdrawAccount.checkSamePassword(accountTransferReqDto.getWithdrawPassword());

        // 출금계좌 잔액
        withdrawAccount.checkBalance(accountTransferReqDto.getAmount());

        // 잊체
        withdrawAccount.withdraw(accountTransferReqDto.getAmount());
        depositAccount.deposit(accountTransferReqDto.getAmount());

        assertThat(withdrawAccount.getBalance()).isEqualTo(900L);
        assertThat(depositAccount.getBalance()).isEqualTo(1100L);

    }

}