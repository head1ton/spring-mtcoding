package ai.springmtcoding.service;

import ai.springmtcoding.domain.account.Account;
import ai.springmtcoding.domain.account.AccountRepository;
import ai.springmtcoding.domain.user.User;
import ai.springmtcoding.domain.user.UserRepository;
import ai.springmtcoding.dto.account.AccountReqDto.AccountSaveReqDto;
import ai.springmtcoding.handler.ex.CustomApiException;
import ai.springmtcoding.dto.account.AccountRespDto.AccountSaveRespDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    // 고객별 계좌목록 보기
    public AccountListRespDto accountList_user(Long userId) {
        User userPS = userRepository.findById(userId).orElseThrow(
            () -> new CustomApiException("고객을 찾을 수 없습니다.")
        );

        List<Account> accountListPS = accountRepository.findByUser_id(userId);
        return new AccountListRespDto(userPS, accountListPS);
    }

    @Getter
    @Setter
    public static class AccountListRespDto {

        private String fullname;
        private List<AccountDto> accounts = new ArrayList<>();

        public AccountListRespDto(User user, final List<Account> accounts) {
            this.fullname = user.getFullname();
            this.accounts = accounts.stream().map(AccountDto::new).collect(
                Collectors.toList());
        }

        @Getter
        @Setter
        public static class AccountDto {

            private Long id;
            private Long number;
            private Long balance;

            public AccountDto(Account account) {
                this.id = account.getId();
                this.number = account.getNumber();
                this.balance = account.getBalance();
            }
        }
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
}
