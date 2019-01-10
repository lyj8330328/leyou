import com.leyou.LyGoodsWebApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 98050
 * @Time: 2018-10-25 22:58
 * @Feature:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyGoodsWebApplication.class)
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String KEY_PREFIX = "leyou:seckill:test";

    @Test
    public void testRedisObj() {
        //redisTemplate.opsForValue().set("111111111","2222221231232222222");
        //redisTemplate.opsForValue().set("key","1");
        redisTemplate.opsForValue().increment("key",1);
    }


    @Test
    public void test(){
        BoundHashOperations<String,Object,Long> hashOperations = this.stringRedisTemplate.boundHashOps("leyou:goods:detail:1");
        BoundValueOperations<String, String> valueOperations = this.stringRedisTemplate.boundValueOps("leyou:goods:detail:1");
        valueOperations.set("123", 1);
        //hashOperations.increment("123", 2);
        //hashOperations.expire(10, TimeUnit.SECONDS);
    }

    @Test
    public void redisTest(){
        Map<String,String> result = new HashMap<>();
        BoundHashOperations<String,Object,Object> hashOperations = this.stringRedisTemplate.boundHashOps(KEY_PREFIX);
        hashOperations.put("1", "22");
        hashOperations.put("2", "22");
        hashOperations.put("3", "22");


        System.out.println("---------------------------------------------------------");
        hashOperations.entries().forEach((m,n) ->{
            System.out.println("key：" + m + "," + n);
        });
    }
}
