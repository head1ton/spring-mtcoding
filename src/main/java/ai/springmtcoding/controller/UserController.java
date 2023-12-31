package ai.springmtcoding.controller;

import ai.springmtcoding.dto.ResponseDto;
import ai.springmtcoding.dto.user.UserReqDto.JoinReqDto;
import ai.springmtcoding.dto.user.UserRespDto.JoinRespDto;
import ai.springmtcoding.service.UserService;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid JoinReqDto joinReqDto,
        BindingResult bindingResult) {

        JoinRespDto joinRespDto = userService.join(joinReqDto);

        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", joinRespDto),
            HttpStatus.CREATED);
    }

}
