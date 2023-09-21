package ai.springmtcoding.service;

import ai.springmtcoding.config.auth.LoginUser;
import ai.springmtcoding.domain.account.Account;
import ai.springmtcoding.domain.account.AccountRepository;
import ai.springmtcoding.domain.user.User;
import ai.springmtcoding.domain.user.UserRepository;
import ai.springmtcoding.dto.account.AccountReqDto.AccountSaveReqDto;
import ai.springmtcoding.dto.account.AccountRespDto.AccountListRespDto;
import ai.springmtcoding.handler.ex.CustomApiException;
import ai.springmtcoding.dto.account.AccountRespDto.AccountSaveRespDto;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
}
