package ai.springmtcoding.config.dummy;

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
    CommandLineRunner init(UserRepository userRepository) {
        return (args) -> {
            User test = userRepository.save(newUser("test", "테스터"));
        };
    }
}
