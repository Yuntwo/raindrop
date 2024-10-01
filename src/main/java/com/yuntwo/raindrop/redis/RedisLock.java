package com.yuntwo.raindrop.redis;

import com.yuntwo.raindrop.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author yuntwo
 * Redis实现分布式锁，最佳实践
 * 1、调用Redis自身的key过期时间特性
 * 2、使用唯一标识符作为value，确保只有拥有这个标识符(创建锁)的客户端才能释放锁
 */
@Component
@Slf4j
public class RedisLock {

    @Autowired
    private RedisTemplate redisTemplate;

    public String lock(String key, long timeout, TimeUnit unit) {
        String value = UUIDUtil.getId();
        // 调用Redis设置过期key的方法，Redis底层策略确保过期时间删除key
        boolean lock = redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
        if (!lock) {
            return null;
        }
        return value;
    }

    public void unlock(String key, String value) {
        try {
            String currentValue = (String) redisTemplate.opsForValue().get(key);
            // 确保创建这个key的客户端才能删除这个key
            if (value.equals(currentValue)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            log.error("【redis解锁错误】" + e);
        }
    }
}

/**
 * @author yangshu
 * Redis实现分布式锁，非最佳实践
 */
/*
@Component
@Slf4j
public class RedisLock {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 加锁
     * @param key 分布式锁的名字
     * @param value  当前的时间 + 超时时间长度 = 锁的过期时间
     * @return 是否成功获取锁
     * /
    public boolean lock(String key,String value){
        // 对应的Redis操作语句是set key value NX
        if (!redisTemplate.opsForValue().setIfAbsent(key,value)) {
            return true;
        }

        // 当这个key不存在时get方法会返回null来表明这是一个不存在的key而不是抛出异常
        String currentValue = (String) redisTemplate.opsForValue().get(key);

        // 用过期时间来避免死锁，这里是惰性删除实现
        // 当然过期时间到的时候这个锁还存在有两种情况
        // 1、线程忘记释放锁造成死锁
        // 2、线程还没有执行完
        // key存在但是它的value过期时间小于当前时间说明已经过期
        if(!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()){
            // 获取上一个锁的时间，并且设置新的过期时间
            // 如果在这个语句之前没有其他线程获取锁则oldValue和currentValue应该相同，说明上锁成功
            String oldValue = (String) redisTemplate.opsForValue().getAndSet(key,value);
            if(!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)){
                return true;
            }
        }
        return false;
    }

    public void unlock(String key,String value){
        // 主要捕获的是Redis连接等其他异常而不是delete异常
        try {
            String currentValue = (String) redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                // 这个操作本身是不会抛出异常的，允许key不存在、为null
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        }
        catch (Exception e){
            log.error("【redis解锁错误】" + e);
        }

    }

}
*/

