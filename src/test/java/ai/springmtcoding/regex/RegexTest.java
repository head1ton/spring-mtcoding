package ai.springmtcoding.regex;

import java.util.regex.Pattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RegexTest {

    @Test
    public void 한글만된다_test() throws Exception {
        String value = "한글";
        boolean result = Pattern.matches("^[가-힣]+$", value);
        System.out.println("result = " + result);
    }

    @Test
    public void 한글은안된다_test() throws Exception {
        String value = "abc";
        boolean result = Pattern.matches("^[^ㄱ-ㅎ가-힣]*$", value);
        System.out.println("result = " + result);
    }

    @Test
    public void 영어만된다_test() throws Exception {
        String value = "test";
        boolean result = Pattern.matches("^[a-zA-Z]+$", value);
        System.out.println("result = " + result);
    }

    @Test
    public void 영어는안된다_test() throws Exception {
        String value = "test";
        boolean result = Pattern.matches("^[^a-zA-Z]*$", value);
        System.out.println("result = " + result);
    }

    @Test
    public void 영어와숫자만된다_test() throws Exception {
        String value = "ab12";
        boolean result = Pattern.matches("^[a-zA-Z0-9]+$", value);
        System.out.println("result = " + result);
    }

    @Test
    public void 영어만되고_길이는최소2최대4이다_test() throws Exception {
        String value = "test";
        boolean result = Pattern.matches("^[a-zA-Z]{2,4}$", value);
        System.out.println("result = " + result);
    }

    @Test
    @DisplayName("user_username_test")
    public void user_username_test() throws Exception {
        String username = "test";
        boolean result = Pattern.matches("^[a-zA-Z0-9]{2,20}$", username);
        System.out.println("result = " + result);
    }

    @Test
    @DisplayName("user_email_test")
    public void user_email_test() throws Exception {
        String email = "test@gmail.com";
        boolean result = Pattern.matches("^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$",
            email);
        System.out.println("result = " + result);
    }

    @Test
    @DisplayName("account_gubun_test1")
    public void account_gubun_test1() throws Exception {
        String gubun = "DEPOSIT";
        boolean result = Pattern.matches("^[DELPOSIT]$", gubun);
        System.out.println("result = " + result);
    }

    @Test
    @DisplayName("account_gubun_test2")
    public void account_gubun_test2() throws Exception {
        String gubun = "TRANSFER";
        boolean result = Pattern.matches("^(DELPOSIT|TRANSFER)$", gubun);
        System.out.println("result = " + result);
    }

    @Test
    @DisplayName("account_tel_test1")
    public void account_tel_test1() throws Exception {
        String tel = "010-3333-7777"; // ac.kr co.kr or.kr
        boolean result = Pattern.matches("^[0-9]{3}-[0-9]{4}-[0-9]{4}", tel);
        System.out.println("테스트 : " + result);
    }

    @Test
    @DisplayName("account_tel_test2")
    public void account_tel_test2() throws Exception {
        String tel = "01033337777"; // ac.kr co.kr or.kr
        boolean result = Pattern.matches("^[0-9]{11}", tel);
        System.out.println("테스트 : " + result);
    }


}
