import com.leyou.auth.LyAuthApplication;
import com.leyou.auth.service.AuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

/**
 * @Author: 98050
 * @Time: 2018-11-15 00:00
 * @Feature: 获取5000用户的Token，写入文件中
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyAuthApplication.class)
public class TokenTest {

    @Autowired
    private AuthService authService;

    @Test
    public void getToken() throws IOException {
        File userInfo = new File("G:/userInfo.txt");
        if (userInfo.exists()){
            userInfo.delete();
        }
        FileWriter fw = new FileWriter(userInfo, true);
        BufferedWriter bw = new BufferedWriter(fw);
        for (int i =0;i < 50 ; i++) {
            String token = this.authService.authentication("username"+i,"abcdefg"+i);
            bw.append("username"+i+","+token+"\r\n");
        }
        bw.close();
        fw.close();

    }

    @Test
    public void getTokenCSV(){
        try {
            File csv = new File("G://Token.csv");//CSV文件
            BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true));
            for (int i =0; i < 50; i++) {
                //新增一行数据
                String token = this.authService.authentication("username"+i,"abcdefg"+i);
                bw.write("username"+i+","+token);
                bw.newLine();
            }
            bw.close();
        } catch (FileNotFoundException e) {
            //捕获File对象生成时的异常
            e.printStackTrace();
        } catch (IOException e) {
            //捕获BufferedWriter对象关闭时的异常
            e.printStackTrace();
        }
    }
}
