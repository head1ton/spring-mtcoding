package ai.springmtcoding.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ai.springmtcoding.config.dummy.DummyObject;
import ai.springmtcoding.domain.user.User;
import ai.springmtcoding.domain.user.UserEnum;
import ai.springmtcoding.domain.user.UserRepository;
import ai.springmtcoding.dto.user.UserReqDto;
import ai.springmtcoding.dto.user.UserReqDto.JoinReqDto;
import ai.springmtcoding.dto.user.UserRespDto.JoinRespDto;
import ai.springmtcoding.handler.ex.CustomApiException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest extends DummyObject {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("join_test")
    public void join_test() {
        JoinReqDto joinReqDto = new UserReqDto.JoinReqDto();
        joinReqDto.setUsername("test");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("test@gmail.com");
        joinReqDto.setFullname("테스터");

        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        User test = newMockUser(1L, "test", "테스터");
        when(userRepository.save(any())).thenReturn(test);

        JoinRespDto join = userService.join(joinReqDto);
        System.out.println("join = " + join);

        assertThat(join.getId()).isEqualTo(1L);
        assertThat(join.getUsername()).isEqualTo("test");
    }

    @Test
    @DisplayName("join_fail_test")
    public void join_fail_test() {
        UserReqDto.JoinReqDto joinReqDto = new UserReqDto.JoinReqDto();
        joinReqDto.setUsername("test");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("test@gmail.com");
        joinReqDto.setFullname("테스터");

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(User.builder().build()));

        User test = newMockUser(1L, "test", "테스터");

        assertThatThrownBy(() -> userService.join(joinReqDto))
            .isInstanceOf(CustomApiException.class)
            .hasMessageContaining("동일한 username이 존재합니다.");
    }
}