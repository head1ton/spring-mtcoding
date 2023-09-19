package ai.springmtcoding.service;

import ai.springmtcoding.domain.user.User;
import ai.springmtcoding.domain.user.UserEnum;
import ai.springmtcoding.domain.user.UserRepository;
import ai.springmtcoding.handler.ex.CustomApiException;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public JoinRespDto join(JoinReqDto joinReqDto) {
        Optional<User> userOP = userRepository.findByUsername(joinReqDto.getUsername());
        if (userOP.isPresent()) {
            throw new CustomApiException("동일한 username이 존재합니다.");
        }

        User userPS = userRepository.save(joinReqDto.toEntity(passwordEncoder));

        return new JoinRespDto(userPS);
    }

    @Getter
    @Setter
    public static class JoinRespDto {

        private Long id;
        private String username;
        private String fullname;

        public JoinRespDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.fullname = user.getFullname();
        }
    }

    @Getter
    @Setter
    public static class JoinReqDto {

        private String username;
        private String password;
        private String email;
        private String fullname;

        public User toEntity(BCryptPasswordEncoder passwordEncoder) {
            return User.builder()
                       .username(username)
                       .password(passwordEncoder.encode(password))
                       .email(email)
                       .fullname(fullname)
                       .role(UserEnum.CUSTOM)
                       .build();
        }
    }
}
