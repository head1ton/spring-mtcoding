package ai.springmtcoding.config.dummy;

import ai.springmtcoding.domain.account.Account;
import ai.springmtcoding.domain.account.AccountRepository;
import ai.springmtcoding.domain.user.User;
import ai.springmtcoding.domain.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DummyDevInit extends DummyObject {

    @Profile("dev")
    @Bean
    CommandLineRunner init(
        UserRepository userRepository,
        AccountRepository accountRepository) {
        return (args) -> {
            User test = userRepository.save(newUser("test", "테스터"));
            User cos = userRepository.save(newUser("cos", "코스"));

            Account testAccount1 = accountRepository.save(newAccount(1111L, test));
            Account cosAccount1 = accountRepository.save(newAccount(2222L, cos));
        };
    }
}
