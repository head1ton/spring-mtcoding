package ai.springmtcoding.dto.account;

import ai.springmtcoding.domain.account.Account;
import ai.springmtcoding.domain.transaction.Transaction;
import ai.springmtcoding.domain.user.User;
import ai.springmtcoding.util.CustomDateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

public class AccountReqDto {

    @Getter
    @Setter
    public static class AccountSaveReqDto {

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long number;
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long password;

        public Account toEntity(User user) {
            return Account.builder()
                          .number(number)
                          .password(password)
                          .balance(1000L)
                          .user(user)
                          .build();
        }
    }

    @Setter
    @Getter
    public static class AccountDepositReqDto {

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long number;
        @NotNull
        private Long amount;
        @NotEmpty
        @Pattern(regexp = "DEPOSIT")
        private String gubun;   // DEPOSIT
        @NotEmpty
        @Pattern(regexp = "^[0-9]{11}")
        private String tel;

    }

    @Getter
    @Setter
    public static class AccountWithdrawReqDto {

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long number;
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long password;
        @NotNull
        private Long amount;
        @NotEmpty
        @Pattern(regexp = "WITHDRAW")
        private String gubun;

    }
}