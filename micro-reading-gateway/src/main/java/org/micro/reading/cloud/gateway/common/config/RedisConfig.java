package org.micro.reading.cloud.gateway.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * @author micro-paul
 * @date 2022年03月16日 15:52
 */
@EnableCaching
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redis = new RedisTemplate<>();
        redis.setConnectionFactory(redisConnectionFactory);
        // 如果不进行设置的话，默认使用JdkSerializationRedisSerializer进行数据序列化 （把任何数据保存到redis中时，都需要进行序列化）
        setSerializer(redis);
        return redis;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(o.getClass().getName()).append(method.getName());
                for (Object object : objects) {
                    stringBuilder.append(object.toString());
                }
                return stringBuilder.toString();
            }
        };
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        //初始化一个RedisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        //设置CacheManager的值序列化方式为json序列化
        RedisSerializer<Object> jsonSerializer = new GenericJackson2JsonRedisSerializer();
        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer);
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
        //设置默认超过期时间是30秒
        defaultCacheConfig = defaultCacheConfig.entryTtl(Duration.ofSeconds(300));
        //初始化RedisCacheManager
        return new RedisCacheManager(redisCacheWriter, defaultCacheConfig);

    }

    /**
     * 现在可用的RedisSerializer主要有几种：
     * <p>
     * （1）StringRedisSerializer
     * （2）Jackson2JsonRedisSerializer
     * （3）JdkSerializationRedisSerializer
     * （4）GenericToStringSerializer
     * （5）OxmSerializer
     * <p>
     * StringRedisSerializer：对String数据进行序列化。序列化后，保存到Redis中的数据，不会有像上面的“\xAC\xED\x00\x05t\x00\x09”多余字符。就是"frequency"
     * Jackson2JsonRedisSerializer：用Jackson2，将对象序列化成Json。这个Serializer功能很强大，但在现实中，是否需要这样使用，要多考虑。一旦这样使用后，要修改对象的一个属性值时，就需要把整个对象都读取出来，再保存回去。
     * JdkSerializationRedisSerializer：使用Java序列化。结果就像最上面的样子。
     * GenericToStringSerializer：使用Spring转换服务进行序列化。在网上没有找到什么例子，使用方法和StringRedisSerializer相比，
     * StringRedisSerializer只能直接对String类型的数据进行操作，如果要被序列化的数据不是String类型的，需要转换成String类型，
     * 例如：String.valueOf()。但使用GenericToStringSerializer的话，不需要进行转换，直接由String帮我们进行转换。但这样的话，也就定死了序列化前和序列化后的数据类型，例如：template.setValueSerializer(new GenericToStringSerializer<Long>(Long.class));
     * OxmSerializer：使用SpringO/X映射的编排器和解排器实现序列化，用于XML序列化。
     *
     * @param redisTemplate
     * @author micro-paul
     * @date 2022/3/16 16:12
     */
    private void setSerializer(RedisTemplate<String, Object> redisTemplate) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        //设置键（key）的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        //设置值（value）的序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
    }


}
