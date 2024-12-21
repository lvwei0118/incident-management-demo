package com.incident.management.Redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Description: Redis工具
 * @Author: lvwei
 * @Date: 2024/12/20
 * @Version: V1.0
 */
@Component
public class RedisUtil {

    @Resource
    RedisTemplate<String,Object> redisTemplate;

    /**
     * 设置Redis键值对
     * set key
     *
     * @param key Redis键
     * @param value Redis值
     *
     * opsForValue().set(key, value)：设置指定 key 的值为指定的 value。
     * 添加数据
     */
    public boolean set(final String key,final Object value){
        ValueOperations<String,Object> operations = redisTemplate.opsForValue();
        operations.set(key, value);
        return true;
    }

    /**
     * 设置Redis键值对
     * set key
     *
     * @param key Redis键
     * @param value Redis值
     * @param time 过期时间
     * opsForValue().set(key, value,time)：设置指定 key 的值为指定的 value,并设置过期时间。。
     * 添加数据
     */
    public boolean set(final String key,final Object value,final long time){
        ValueOperations<String,Object> operations = redisTemplate.opsForValue();
        operations.set(key, value, time, TimeUnit.SECONDS);
        return true;
    }


    /**
     * 根据Redis键，自增1
     * incur key
     *
     * @param key Redis键
     * @return
     */
    public Long incur(final String key){
        ValueOperations<String,Object> operations = redisTemplate.opsForValue();
        return operations.increment(key);
    }

    /**
     * 根据key的值，查询value
     * 根据 key：**_phone 查询 value：也就是电话号码
     * 也就是分别对应 键+值
     * opsForValue().get(key)：获取指定 key 的值。
     */
    public Object get(final String key){
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除指定数据
     * key
     */
    public boolean delete(String key){
        return redisTemplate.delete(key);
    }

    /**
     * 更新数据
     * opsForValue().set(key, value)：更新指定 key 的值为指定的 value。
     */
    public boolean update(final String key,final Object value){
        ValueOperations<String,Object> operations = redisTemplate.opsForValue();
        operations.set(key, value);
        return true;
    }
}
