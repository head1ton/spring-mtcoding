package ai.springmtcoding.config.jwt;

import ai.springmtcoding.config.auth.LoginUser;
import ai.springmtcoding.domain.user.User;
import ai.springmtcoding.domain.user.UserEnum;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;

public class JwtProcess {

    // token create
    public static String create(LoginUser loginUser) {
        String jwtToken = JWT.create()
                             .withSubject("code test")
                             .withExpiresAt(
                                 new Date(System.currentTimeMillis() + JwtVO.EXPIRATION_TIME))
                             .withClaim("id", loginUser.getUser().getId())
                             .withClaim("role", loginUser.getUser().getRole() + "")
                             .sign(Algorithm.HMAC512(JwtVO.SECRET));
        return JwtVO.TOKEN_PRIFIX + jwtToken;
    }

    // token 검증
    public static LoginUser verify(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVO.SECRET)).build().verify(token);
        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();
        User user = User.builder().id(id).role(UserEnum.valueOf(role)).build();
        return new LoginUser(user);
    }

}
