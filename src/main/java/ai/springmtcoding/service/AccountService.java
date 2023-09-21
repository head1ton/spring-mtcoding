package ai.springmtcoding.service;

import ai.springmtcoding.domain.account.Account;
import ai.springmtcoding.domain.account.AccountRepository;
import ai.springmtcoding.domain.transaction.Transaction;
import ai.springmtcoding.domain.transaction.TransactionEnum;
import ai.springmtcoding.domain.transaction.TransactionRepository;
import ai.springmtcoding.domain.user.User;
import ai.springmtcoding.domain.user.UserRepository;
import ai.springmtcoding.dto.account.AccountReqDto.AccountDepositReqDto;
import ai.springmtcoding.dto.account.AccountReqDto.AccountSaveReqDto;
import ai.springmtcoding.dto.account.AccountRespDto.AccountDepositRespDto;
import ai.springmtcoding.dto.account.AccountRespDto.AccountListRespDto;
import ai.springmtcoding.handler.ex.CustomApiException;
import ai.springmtcoding.dto.account.AccountRespDto.AccountSaveRespDto;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    // 고객별 계좌목록 보기
    public AccountListRespDto accountList_user(Long userId) {
        User userPS = userRepository.findById(userId).orElseThrow(
            () -> new CustomApiException("고객을 찾을 수 없습니다.")
        );

        List<Account> accountListPS = accountRepository.findByUserId(userId);
        return new AccountListRespDto(userPS, accountListPS);
    }

    @Transactional
    public AccountSaveRespDto accountCreate(AccountSaveReqDto accountSaveReqDto, Long userId) {
        // User가 DB에 있는지
        User userPS = userRepository.findById(userId).orElseThrow(
            () -> new CustomApiException("고객을 찾을 수 없습니다.")
        );

        // 해당 계좌가 DB에 있는지
        Optional<Account> accountOP = accountRepository.findByNumber(accountSaveReqDto.getNumber());
        if (accountOP.isPresent()) {
            throw new CustomApiException("해당 계좌가 이미 존재합니다.");
        }

        // 계좌 등록
        Account accountPS = accountRepository.save(accountSaveReqDto.toEntity(userPS));

        // DTO 로 응답
        return new AccountSaveRespDto(accountPS);
    }

    @Transactional
    public void accountDelete(Long number, Long userId) {
        Account accountPS = accountRepository.findByNumber(number).orElseThrow(
            () -> new CustomApiException("계좌를 찾을 수 없습니다.")
        );

        accountPS.checkOwner(userId);

        accountRepository.deleteById(accountPS.getId());
    }

    @Transactional
    public AccountDepositRespDto accountDeposit(AccountDepositReqDto accountDepositReqDto) {
        // 0원 체크
        if (accountDepositReqDto.getAmount() <= 0L) {
            throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
        }

        // 입금 계좌 확인
        Account depositAccountPS = accountRepository.findByNumber(accountDepositReqDto.getNumber())
                                                    .orElseThrow(
                                                        () -> new CustomApiException(
                                                            "계좌를 찾을 수 없습니다."));

        // 입금 (해당 계좌 balance 조정 - update 문
        depositAccountPS.deposit(accountDepositReqDto.getAmount());

        // 거래 내역
        Transaction transaction = Transaction.builder()
                                             .withdrawAccount(null)
                                             .depositAccount(depositAccountPS)
                                             .withdrawAccountBalance(null)
                                             .depositAccountBalance(depositAccountPS.getBalance())
                                             .amount(accountDepositReqDto.getAmount())
                                             .gubun(TransactionEnum.DEPOSIT)
                                             .sender("ATM")
                                             .receiver(accountDepositReqDto.getNumber() + "")
                                             .tel(accountDepositReqDto.getTel())
                                             .build();

        Transaction transactionPS = transactionRepository.save(transaction);

        return new AccountDepositRespDto(depositAccountPS, transactionPS);
    }

}
