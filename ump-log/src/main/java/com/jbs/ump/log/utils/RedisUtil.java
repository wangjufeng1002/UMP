package com.jbs.ump.log.utils;

import com.alibaba.fastjson.JSONObject;

import com.jbs.ump.log.entity.Redis;
import com.jbs.ump.log.enums.RedisKeyEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @created by wjf
 * @date 2019/11/20 14:02
 * @description:
 */

public class RedisUtil {
    private final static Logger log = LoggerFactory.getLogger(RedisUtil.class);
    private Redis redis; // = new Redis(); // redis
    private static JedisPool pool = null; // jedis pool

    public RedisUtil(Redis redis) {
        try {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxIdle(redis.getMaxIdle());
            jedisPoolConfig.setMaxTotal(redis.getMaxTotal());
            jedisPoolConfig.setTestOnBorrow(redis.isTestOnBorrow());
            jedisPoolConfig.setTestOnReturn(redis.isTestOnReturn());
            // 根据配置实例化jedis池
            if (StringUtils.isNotEmpty(redis.getPassword())) {
                pool = new JedisPool(jedisPoolConfig, redis.getIp(), redis.getPort(), redis.getTimeout(),
                        redis.getPassword());
            } else {
                pool = new JedisPool(jedisPoolConfig, redis.getIp(), redis.getPort(), redis.getTimeout());
            }
            this.redis = redis;
        } catch (Exception e) {
            log.error("获取redisProps : " + e.getMessage(), e);
        }
    }

    public RedisUtil() {
    }

    /***
     *  向内存数据库写值
     * @param key   键
     * @param value 值
     */
    public void redisSet(String key, String value) {
        Jedis jedis = pool.getResource();
        jedis.set(key, value);
        jedis.close();
    }

    public String redisSet(RedisKeyEnum keyEnum, String value) {
        String key = keyEnum.formatKey(new Date().getTime());
        Jedis jedis = pool.getResource();
        jedis.set(key, value);
        jedis.setex(key, keyEnum.getExpireTime(), value);
        log.info("Redis redisSet " + key + ":" + keyEnum.getExpireTime() + ":" + value);
        return key;

    }

    /***
     *  添加数据到redis中
     * @param key      键
     * @param seconds  有效时间
     * @param value    值
     */
    public void redisSet(String key, int seconds, String value) {
        Jedis jedis = pool.getResource();
        jedis.setex(key, seconds, value);
        log.debug("Redis Setex " + key + ":" + seconds + ":" + value);
        jedis.close();
        ;
    }

    /**
     * 插入 hash 结构数据
     *
     * @param key   键
     * @param field 字段
     * @param value 值
     */
    public void redisSetHash(String key, String field, String value) {
        Jedis jedis = pool.getResource();
        jedis.hset(key, field, value);

        log.debug("Redis Setex " + key + ":" + key + ": field: " + field + " value: " + value);
        jedis.close();
    }

    public Map<String, String> redisHashGetAll(String key) {
        Jedis jedis = pool.getResource();
        Map<String, String> all = jedis.hgetAll(key);
        jedis.close();
        log.debug("Redis hGetAll " + key + ":" + JSONObject.toJSONString(all));
        return all;
    }

    /**
     * 删除key
     *
     * @param key   键
     * @param field 值
     */
    public void redisHashDel(String key, String field) {
        Jedis jedis = pool.getResource();
        jedis.hdel(key, field);
        log.debug("Redis delete " + key + ":" + key + ": field: " + field);
        jedis.close();
    }

    /**
     * 插入map 对象
     *
     * @param key key
     * @param map map
     */
    public void redisSetHash(String key, Map<String, String> map) {
        Jedis jedis = pool.getResource();
        jedis.hset(key, map);
        log.debug("Redis Setex " + key + ":" + key + ": value: " + JSONObject.toJSONString(map));
        jedis.close();
    }

    public void redisHashIncr(String key, String field, long incr) {
        Jedis jedis = pool.getResource();
        jedis.hincrBy(key, field, incr);
        log.debug("Redis hincrBy " + key + ":" + key + ": field: " + field + " incr:" + incr);
        jedis.close();
    }

    /**
     * 插入map 对象
     *
     * @param key key
     * @param map map
     */
    public void redisSetHash(String key, Map<String, String> map, Integer seconds) {
        Jedis jedis = pool.getResource();
        jedis.hset(key, map);
        jedis.expire(key, seconds);
        log.debug("Redis Setex " + key + ":" + key + ": value: " + JSONObject.toJSONString(map));
        jedis.close();
    }

    /**
     * 获取hash 结构中某一字段的值
     *
     * @param key   键
     * @param field 字段
     * @return 值
     */
    public String redisHashGetValue(String key, String field) {
        Jedis jedis = pool.getResource();
        String value = jedis.hget(key, field);
        jedis.close();
        return value;
    }

    /**
     * 获取hash 结构中所有field 字段
     *
     * @param key 键
     * @return 所有field 字段
     */
    public Set<String> redisHashGetKeys(String key) {
        Jedis jedis = pool.getResource();
        Set<String> hkeys = jedis.hkeys(key);
        jedis.close();
        return hkeys;
    }

    public Boolean redisHashExistKey(String key, String field) {
        Jedis jedis = pool.getResource();
        Boolean hexists = jedis.hexists(key, field);
        jedis.close();
        return hexists;
    }

    public Long redisAddSet(String key, String... value) {
        Jedis jedis = pool.getResource();
        Long sadd = jedis.sadd(key, value);
        jedis.close();
        return sadd;
    }

    public Jedis getJedisConnect() {
        return pool.getResource();
    }

    /**
     * 将对象以Json的格式放入Redis中
     *
     * @param key    键
     * @param object 对象
     */
    public void redisSet(String key, Object object, int expireTime) {
        try {
            if (object != null) {
                if (expireTime <= 0) {
                    int seconds = 24 * 60 * 60;
                    this.redisSet(key, seconds, JSONObject.toJSONString(object));
                } else {
                    this.redisSet(key, expireTime, JSONObject.toJSONString(object));
                }
            }
        } catch (Exception e) {
            log.info("redisSetObj Exception " + key + ":" + object);
        }

    }


    /**
     * 更新redis中的数据
     *
     * @param key     键
     * @param seconds 过期时间
     * @param value   值
     */
    public void redisUpdate(String key, int seconds, String value) {
        Jedis jedis = pool.getResource();
        jedis.del(key);
        jedis.setex(key, seconds, value);
        jedis.close();
        ;
    }

    /***
     *  从redis中删除数据
     * @param key 键
     */
    public void redisDel(String key) {
        Jedis jedis = pool.getResource();
        jedis.del(key);
        jedis.close();
        ;
    }

    /***
     * 从redis中获取数据
     * @param key 键
     * @return 值
     */
    public String redisGet(String key) {
        Jedis jedis = pool.getResource();
        String value = jedis.get(key);
        jedis.close();
        log.debug("Redis Get " + key + ":" + value);
        return value;
    }


    /**
     * 设置key值的过期时间
     *
     * @param key  键
     * @param time 过期时间
     */
    public void setRedisTime(String key, int time) {
        Jedis jedis = pool.getResource();
        jedis.expire(key, time);
        log.info("Redis settime " + key + ":" + time);
//			pool.returnResource(jedis);
        jedis.close();
    }

    /***
     * 从redis中批量获取Key
     * @param pattarn 获取规则
     * @return 所有的key
     */
    public Set<String> redisKeys(String pattarn) {
        Jedis jedis = pool.getResource();
        Set<String> keys = jedis.keys(pattarn);
        jedis.close();
        return keys;
    }

    /**
     * 自增
     *
     * @param key 键
     * @return 自增后的值
     */
    public Long redisIncr(String key) {
        Jedis jedis = pool.getResource();
        Long incr = jedis.incr(key);
        jedis.close();
        return incr;
    }

    /**
     * 设置过期时间
     *
     * @param key     键
     * @param seconds 过期时间
     * @return 过期时间
     */
    @SuppressWarnings({"unchecked"})
    public void redisExpire(String key, int seconds) {
        Jedis jedis = pool.getResource();
        jedis.expire(key, seconds);
        jedis.close();
    }


    /***
     * @desc redis监听某个频道的消息
     * @param jedisPubSub
     * @param channel
     */
    @SuppressWarnings({"unchecked"})
    public void subscribe(final JedisPubSub jedisPubSub, final String channel) {
        new Thread() {
            public void run() {
                Jedis jedis = pool.getResource();
                jedis.subscribe(jedisPubSub, channel);
                jedis.close();
            }
        }.start();
    }

    /**
     * 读取redis中的Key并生成对象
     *
     * @param key   键
     * @param clazz 对象class
     */
    public <T> T redisGetObj(String key, Class<T> clazz) throws Exception {
        T result = null;
        String value = redisGet(key);
        if (StringUtils.isNotBlank(value)) {
            return JSONObject.parseObject(value, clazz);
        }
        return result;
    }

    /**
     * 获取对象列表
     *
     * @param key   key
     * @param clazz 对象class
     * @return 对象集合
     */
    @SuppressWarnings({"unchecked"})
    public <T> List<T> redisGetList(String key, Class<T> clazz) {
        List<T> result = null;
        String value = redisGet(key);
        if (StringUtils.isNotBlank(value)) {
            return JSONObject.parseArray(value, clazz);
        }
        return result;
    }

}
