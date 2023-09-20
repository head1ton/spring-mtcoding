package ai.springmtcoding.dto.user;

import ai.springmtcoding.domain.user.User;
import ai.springmtcoding.domain.user.UserEnum;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserReqDto {

    @Getter
    @Setter
    public static class LoginReqDto {

        private String username;
        private String password;
    }

    @Getter
    @Setter
    public static class JoinReqDto {

        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자 2~20자 이내로 작성해 주세요.")
        @NotEmpty
        private String username;

        @NotEmpty
        @Size(min = 4, max = 20)
        private String password;

        @NotEmpty
        @Pattern(regexp = "^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$", message = "이메일 형식으로 작성해 주세요.")
        private String email;

        @NotEmpty
        @Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$", message = "한글/영문 1~20자 이내로 작성해 주세요.")
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