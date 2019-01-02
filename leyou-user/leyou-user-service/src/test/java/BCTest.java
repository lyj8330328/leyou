import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Author: 98050
 * @Time: 2018-11-06 15:13
 * @Feature: BCryptPasswordEncoder测试
 */
public class BCTest {

    @Test
    public void BcTest(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = "123123";
        System.out.println("加密前：" + password);
        //System.out.println("加密后：" + bCryptPasswordEncoder.encode(password));
        System.out.println("密码匹配" + bCryptPasswordEncoder.matches(password,"$2a$10$sqKOb9/8gSCAG2kJeZ3Ts.tW2JGtD8sbx00GyknYjSuWbBiMjUuOi"));
    }
}
