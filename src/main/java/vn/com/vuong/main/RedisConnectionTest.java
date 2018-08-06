package vn.com.vuong.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import vn.com.vuong.config.ApplicationConfig;

public class RedisConnectionTest {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		RedisConnectionFactory redisConnectionFactory = (RedisConnectionFactory) context.getBean("redisConnectionFactory");
		RedisConnection connection = redisConnectionFactory.getConnection();
		connection.set("lastname".getBytes(),"Vuong".getBytes());
		System.out.println(new String(connection.get("lastname".getBytes())));
	}
}
