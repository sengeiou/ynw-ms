package com.ynw.system.util;

import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private BoundListOperations<String, Object> listRedisTemplate;

    /**
     * 获取
     *
     * @param key
     *            键
     * @return
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 存入
     *
     * @param key
     *            键
     * @param value
     *            值
     */
    public void put(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 存入
     *
     * @param key
     * @param value
     * @param exp,有效时间
     */
    public void put(String key, Object value, long exp) {
        redisTemplate.opsForValue().set(key, value, exp, TimeUnit.SECONDS);
    }

    /**
     * 保存 json 字符串
     * @param key
     * @param value
     *            待保存的JSON字符串
     */
    public void pushMag(String key, String value) {
        listRedisTemplate = redisTemplate.boundListOps(key);
        listRedisTemplate.leftPush(value);
    }

    /**
     * Redis的List数据操作 保存List 集合
     * @param key 键
     * @param list 数据集
     */
    public Long forListLeftPush(String key, List<Object> list) {
        return redisTemplate.opsForList().leftPush(key, list);
    }

    /**
     * Redis的List数据操作 读取 List 集合
     * @param key 键
     * @return
     */
    public Object forListLeftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 移除
     * @param key 键
     */
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String ... key){
        if(key!=null && key.length>0){
            if(key.length==1){
                redisTemplate.delete(key[0]);
            }else{
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key){ return redisTemplate.hasKey(key); }


}
