package ai.springmtcoding.dto.account;

import ai.springmtcoding.domain.account.Account;
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
}