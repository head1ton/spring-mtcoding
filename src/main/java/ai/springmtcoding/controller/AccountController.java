package ai.springmtcoding.controller;

import ai.springmtcoding.config.auth.LoginUser;
import ai.springmtcoding.dto.ResponseDto;
import ai.springmtcoding.dto.account.AccountReqDto;
import ai.springmtcoding.dto.account.AccountReqDto.AccountDepositReqDto;
import ai.springmtcoding.dto.account.AccountReqDto.AccountDepositRespDto;
import ai.springmtcoding.dto.account.AccountRespDto.AccountListRespDto;
import ai.springmtcoding.dto.account.AccountRespDto.AccountSaveRespDto;
import ai.springmtcoding.handler.ex.CustomApiException;
import ai.springmtcoding.service.AccountService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/s/account")
    public ResponseEntity<?> saveAccount(
        @RequestBody @Valid AccountReqDto.AccountSaveReqDto accountSaveReqDto,
        BindingResult bindingResult,
        @AuthenticationPrincipal LoginUser loginUser) {

        AccountSaveRespDto accountSaveRespDto = accountService.accountCreate(accountSaveReqDto,
            loginUser.getUser().getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "계좌등록 성공", accountSaveRespDto),
            HttpStatus.CREATED);
    }

    @GetMapping("/s/account/login-user")
    public ResponseEntity<?> findUserAccount(
        @AuthenticationPrincipal LoginUser loginUser
    ) {
        AccountListRespDto accountListRespDto = accountService.accountList_user(
            loginUser.getUser().getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "계좌목록보기_고객별 성공", accountListRespDto),
            HttpStatus.OK);
    }

    @DeleteMapping("/s/account/{number}")
    public ResponseEntity<?> deleteAccount(
        @PathVariable Long number,
        @AuthenticationPrincipal LoginUser loginUser
    ) {
        accountService.accountDelete(number, loginUser.getUser().getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 삭제 완료", null), HttpStatus.OK);
    }

    @PostMapping("/account/deposit")
    public ResponseEntity<?> depositAccount(
        @RequestBody @Valid AccountDepositReqDto accountDepositReqDto,
        BindingResult bindingResult) {
        AccountDepositRespDto accountDepositRespDto = accountService.accountDeposit(
            accountDepositReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 입금 완료", accountDepositRespDto),
            HttpStatus.CREATED);
    }

}
