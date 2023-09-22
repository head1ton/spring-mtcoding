package ai.springmtcoding.dto.account;

import ai.springmtcoding.domain.account.Account;
import ai.springmtcoding.domain.transaction.Transaction;
import ai.springmtcoding.domain.user.User;
import ai.springmtcoding.util.CustomDateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

public class AccountRespDto {

    @Setter
    @Getter
    public static class AccountSaveRespDto {

        private Long id;
        private String username;
        private Long number;
        private Long balance;

        public AccountSaveRespDto(Account account) {
            this.id = account.getId();
            this.username = account.getUser().getUsername();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }
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

    @Getter
    @Setter
    public static class AccountWithdrawRespDto {

        private Long id;
        private Long number;
        private Long balance;
        private TransactionDto transaction;

        public AccountWithdrawRespDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transaction = new TransactionDto(transaction);
        }

        @Getter
        @Setter
        public class TransactionDto {

            private Long id;
            private String gubun;
            private String sender;
            private String reciver;
            private Long amount;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.sender = transaction.getSender();
                this.reciver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }
}