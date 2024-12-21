package com.incident.management.Redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Description: Redis配置
 * @Author: lvwei
 * @Date: 2024/12/20
 * @Version: V1.0
 */
@Configuration
public class RedisConfig {
    /**
     * @Param redisConnectionFactory redis连接工厂
     * @return
     */

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){

        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        //设置Redis连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        //设置key的序列化策略
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //设置value的序列化策列
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

        return redisTemplate;
    }
}
