package ai.springmtcoding.config.dummy;

import ai.springmtcoding.domain.account.Account;
import ai.springmtcoding.domain.user.User;
import ai.springmtcoding.domain.user.UserEnum;
import java.time.LocalDateTime;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class DummyObject {

    protected static User newUser(String username, String fullname) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");
        return User.builder()
                   .username(username)
                   .password(encPassword)
                   .email(username + "@gmail.com")
                   .fullname(fullname)
                   .role(UserEnum.CUSTOM)
                   .build();
    }

    protected User newMockUser(Long id, String username, String fullname) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");
        return User.builder()
                   .id(id)
                   .username(username)
                   .password(encPassword)
                   .email(username + "@gmail.com")
                   .fullname(fullname)
                   .role(UserEnum.CUSTOM)
                   .createdAt(LocalDateTime.now())
                   .updatedAt(LocalDateTime.now())
                   .build();
    }

    protected Account newAccount(Long number, User user) {
        return Account.builder()
                      .number(number)
                      .password(1234L)
                      .balance(1000L)
                      .user(user)
                      .build();
    }

    protected Account newMockAccount(Long id, Long number, Long balance, User user) {
        return Account.builder()
                      .id(id)
                      .number(number)
                      .password(1234L)
                      .balance(balance)
                      .user(user)
                      .createdAt(LocalDateTime.now())
                      .updatedAt(LocalDateTime.now())
                      .build();
    }
}
