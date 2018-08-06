package vn.com.vuong.main;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import vn.com.vuong.config.ApplicationConfig;
import vn.com.vuong.entity.Person;

public class TemplateTest {
	static ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
	static RedisTemplate<String, Person> redisTemplate = (RedisTemplate) context.getBean("redisTemplate");
	static StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) context.getBean("stringRedisTemplate");
	
	public static void stringTemplateTest() {
		ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue(); 
		valueOps.set("vuong", "Bui");
		System.out.println(valueOps.get("vuong"));
	}
	
	public static void simpleValueOperation() {
		ValueOperations<String, Person> valueOperations  = redisTemplate.opsForValue();
		valueOperations.set("vuong", new Person("Vuong", 24, "Ha Noi"));
		System.out.println(valueOperations.get("vuong"));
	}
	
	public static void listValueOperation () {
		ListOperations<String, Person> listOperations =  redisTemplate.opsForList();
		listOperations.rightPush("listPerson", new Person("Vuong", 24, "Ha Noi"));
		listOperations.rightPush("listPerson", new Person("Toan", 16, "Ha Noi"));
		listOperations.leftPush("listPerson", new Person("Dang", 24, "Ha Noi"));
		System.out.println(listOperations.leftPop("listPerson"));// Dang
		System.out.println(listOperations.rightPop("listPerson"));// Toan
		System.out.println(listOperations.index("listPerson", 1)); // Toan
		List<Person> listPerson = listOperations.range("listPerson", 1, 2);
		System.out.println(listPerson);// vuong
	}
	
	public static void setValueOperation () { 
		SetOperations<String, Person> setOperations = redisTemplate.opsForSet();
		setOperations.add("setPerson0", new Person("Vuong", 24, "Ha Noi"));
		setOperations.add("setPerson0", new Person("Tiến", 24, "Ha Noi"));
		
		setOperations.add("setPerson1", new Person("Toan", 16, "Ha Noi"));
		
		setOperations.add("setPerson2", new Person("Dang", 24, "Ha Noi"));
		setOperations.add("setPerson2", new Person("Tiến", 24, "Ha Noi"));
		System.out.println(setOperations.members("setPerson0"));// Lấy tất cả các member của set0
		
		System.out.println(setOperations.difference("setPerson0", "setPerson1")); // Person trong setPerson khác vs setPerson1
		System.out.println(setOperations.union("setPerson0", "setPerson1")); // Gôp 2 Set thành 1
		System.out.println(setOperations.intersect("setPerson0", "setPerson2")); // Person trong set0 và set2
		
		setOperations.remove("setPerson", new Person("Vuong", 24, "Ha Noi")); // Remove person trong Set
		System.out.println(setOperations.pop("setPerson")); // Lấy ra và Remove trong set
	}
	
	public static void hashOperations() {
		HashOperations<String, String, Person> hashOperations =	redisTemplate.opsForHash();
		hashOperations.putIfAbsent("persons", "p1", new Person("Vuong", 24, "Ha Noi")); // Sets the value of a hash hashKey only if hashKey does not exist. 
		hashOperations.put("persons", "p1", new Person("Loan", 24, "Ha Noi")); // Sets the value of a hash hashKey. 
		System.out.println("P1: "+  hashOperations.get("persons", "p1"));//Get value for given hashKey from hash at key.
		System.out.println("Size: "+hashOperations.size("persons"));//Get size of hash at key.
		Map<String, Person> map = hashOperations.entries("persons"); //Get entire hash stored at key.
		System.out.println("Map: " + map);
		System.out.println("Delete: " + hashOperations.delete("persons", "p1"));// Delete given hash hashKeys.
		System.out.println("Map: " + map);
	}
	
	public static void boundListOperations() {
		BoundListOperations<String, Person> cart = redisTemplate.boundListOps("cart");
		System.out.println(cart.rightPop()); // Return and Remove last element bound key
		cart.rightPush(new Person("Vuong", 24, "Ha Noi"));
		cart.rightPush(new Person("Tien", 24, "Ha Noi"));
		cart.rightPush(new Person("Tung", 24, "Ha Noi"));
		System.out.println(cart.rightPop()); // Tung
		System.out.println(cart.leftPop());// Vuong

	}
	
	public static void main(String[] args) {
		stringTemplateTest();
		simpleValueOperation();
		listValueOperation();
		setValueOperation();
		hashOperations();
		boundListOperations();
	}
}
