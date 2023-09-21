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
    public static class AccountDepositRespDto {

        private Long id;    // 계좌 ID
        private Long number;    // 계좌 번호
        private TransactionDto transaction;

        public AccountDepositRespDto(
            Account account,
            Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.transaction = new TransactionDto(transaction);
        }

        @Getter
        @Setter
        public class TransactionDto {

            private Long id;
            private String gubun;
            private String sender;
            private String receiver;
            private Long amount;
            @JsonIgnore
            private Long depositAccountBalance; // 테스트 용도
            private String tel;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.depositAccountBalance = transaction.getDepositAccountBalance();
                this.tel = transaction.getTel();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }
}