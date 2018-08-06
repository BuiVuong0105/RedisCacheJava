package vn.com.vuong.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import redis.clients.jedis.JedisPoolConfig;
import vn.com.vuong.entity.Person;

@Configuration
@ComponentScan(basePackages = { "vn.com.vuong" })
public class ApplicationConfig {

	// Factory tạo ra các connection trong pool
	// Gồm các cấu hình kết nối tới RedisDatabase
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(5);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);

		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
		jedisConnectionFactory.setPoolConfig(poolConfig);
		jedisConnectionFactory.setUsePool(true);
		jedisConnectionFactory.setHostName(DBConfig.HOST);
		jedisConnectionFactory.setPort(DBConfig.PORT);

		return jedisConnectionFactory;
	}

//	@Bean
//	public RedisConnectionFactory redisCF() {
//		LettuceConnectionFactory cf = new LettuceConnectionFactory();
//		cf.setHostName("redis-server");
//		cf.setPort(7379);
//		cf.setPassword("foobared");
//		return cf;
//	}

	// Class giúp thực thi các hoạt động serialization  và deserialization 
	// giữa các object java và Binary data trong Redis Database
	// Thread - safe
	// Mặc định sử dụng JdkSerializationRedisSerializer—Uses
	@Bean
	public RedisTemplate<String, Person> redisTemplate() {
		RedisTemplate<String, Person> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		// RedisTemplate mặc định ko cung cấp quản lý Transaction
		// Cần set Support
		redisTemplate.setEnableTransactionSupport(true);// set support @Transaction
		return redisTemplate;
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate() {
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(redisConnectionFactory());
		stringRedisTemplate.setEnableTransactionSupport(true);
		return stringRedisTemplate;
	}
}
