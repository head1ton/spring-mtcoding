package ai.springmtcoding.dto.user;

import ai.springmtcoding.domain.user.User;
import ai.springmtcoding.domain.user.UserEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserReqDto {

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