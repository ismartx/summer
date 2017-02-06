import com.alibaba.fastjson.JSON;

import org.smartx.summer.redis.template.HashRedisTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Ming on 2016/10/21.
 */
public class TestJedis {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath:redis.xml");
        HashRedisTemplate hashRedisTemplate = (HashRedisTemplate) app.getBean("hashRedisTemplate");
        System.out.println(JSON.toJSONString(hashRedisTemplate.hkeys("USER_SESSION*")));

    }
}
