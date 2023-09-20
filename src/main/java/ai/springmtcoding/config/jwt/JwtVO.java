package ai.springmtcoding.config.jwt;

public interface JwtVO {

    String SECRET = "대한민국역대대통령들외이러나";
    int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;
    String TOKEN_PRIFIX = "Bearer ";
    String HEADER = "Authorization";
}
