package org.micro.reading.cloud.common.cache;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @author micro-paul
 * @date 2022年03月16日 9:43
 */
@Component
public class RedisService {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisService.class);

    /**
     * 默认缓存时间(10分钟)
     */
    public static final Long DEFAULT_CACHE_TIME = RedisExpire.MINUTE_TEN;

    /**
     * 操作字符串
     */
    private ValueOperations<String, String> valueOperations;

    /**
     * 操作hash
     */
    private HashOperations hashOperations;

    /**
     * 操作list
     */
    private ListOperations listOperations;

    /**
     * 操作set
     */
    private SetOperations setOperations;

    /**
     * 操作有序set
     */
    private ZSetOperations zSetOperations;

    /**
     * Redis模板
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @PostConstruct注解好多人以为是Spring提供的。其实是Java自己的注解。 Java中该注解的说明：@PostConstruct该注解被用来修饰一个非静态的void（）方法。被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次。PostConstruct在构造函数之后执行，init（）方法之前执行。
     * 通常我们会是在Spring框架中使用到@PostConstruct注解 该注解的方法在整个Bean初始化中的执行顺序：
     * Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的方法)
     * 应用：在静态方法中调用依赖注入的Bean中的方法。
     * <p>
     * 构造方法初始化操作类
     * @author micro-paul
     * @date 2022/3/16 9:53
     */
    @PostConstruct
    private void init() {
        valueOperations = stringRedisTemplate.opsForValue();
        hashOperations = redisTemplate.opsForHash();
        listOperations = redisTemplate.opsForList();
        setOperations = redisTemplate.opsForSet();
        zSetOperations = redisTemplate.opsForZSet();
    }

    /************************************** 公共方法 */

    /**
     * 根据key删除缓存
     *
     * @param key
     * @return java.lang.Boolean
     * @author micro-paul
     * @date 2022/3/16 9:58
     */
    public Boolean removeCache(String key) {
        Boolean hasExist = hasKey(key);
        if (hasExist) {
            return stringRedisTemplate.delete(key);
        } else {
            return false;
        }
    }

    /**
     * 删除多个key
     *
     * @param keys
     * @return java.lang.Long
     * @author micro-paul
     * @date 2022/3/16 10:05
     */
    public Long removeList(Set<String> keys) {
        if (StringUtils.isEmpty(keys)) {
            return null;
        }
        return stringRedisTemplate.delete(keys);
    }

    /**
     * 获取匹配的key，key可以正则
     *
     * @param keyRegular
     * @return java.util.Set<java.lang.String>
     * @author micro-paul
     * @date 2022/3/16 10:08
     */
    public Set<String> getKeyRegular(String keyRegular) {
        if (StringUtils.isEmpty(keyRegular)) {
            return null;
        }
        return stringRedisTemplate.keys(keyRegular);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param time
     * @author micro-paul
     * @date 2022/3/16 10:11
     */
    public void setExpire(final String key, Long time) {
        stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 获取缓存剩余的时间
     *
     * @param key
     * @return java.lang.Long
     * @author micro-paul
     * @date 2022/3/16 10:12
     */
    public Long getExpire(final String key) {
        return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
    }


    private Boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }
    /************************************** 公共方法 End */

    /************************************** String处理（实体转化为json字符串） */

    /**
     * 获取缓存
     *
     * @param key
     * @return java.lang.String
     * @author micro-paul
     * @date 2022/3/16 10:20
     */
    public String getCache(String key) {
        return valueOperations.get(key);
    }

    /**
     * 设置缓存没有过期时间
     *
     * @param key
     * @param value
     * @author micro-paul
     * @date 2022/3/16 10:39
     */
    public void setCache(String key, String value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return;
        }
        valueOperations.set(key, value);
    }

    /**
     * 设置缓存有默认过期时间
     *
     * @param key
     * @param value
     * @author micro-paul
     * @date 2022/3/16 10:39
     */
    public void setCacheExpireDefault(String key, String value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return;
        }
        valueOperations.set(key, value, DEFAULT_CACHE_TIME, TimeUnit.SECONDS);
    }

    /**
     * 设置缓存过期时间
     *
     * @param key
     * @param value
     * @param expire
     * @author micro-paul
     * @date 2022/3/16 10:43
     */
    public void setCacheExpire(String key, String value, Long expire) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value) || StringUtils.isEmpty(expire) || expire <= 0) {
            return;
        }
        valueOperations.set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * 从缓存里面获取数据
     *
     * @param key
     * @param typeOff
     * @return java.lang.Object
     * @author micro-paul
     * @date 2022/3/16 10:46
     */
    public Object getCache(String key, Type typeOff) {

        return JSONObject.parseObject(getCache(key), typeOff);
    }

    /**
     * 从缓存里面获取数据
     *
     * @param key
     * @param c
     * @return java.lang.Object
     * @author micro-paul
     * @date 2022/3/16 10:46
     */
    public <T> T getCache(String key, Class<T> c) {
        String cache = getCache(key);
        return JSONObject.parseObject(cache, c);
    }

    /**
     * 从缓存里面获取数据
     *
     * @param key
     * @param c
     * @return java.lang.Object
     * @author micro-paul
     * @date 2022/3/16 10:46
     */
    public <T> List getCacheList(String key, Class<T> c) {
        String cache = getCache(key);
        return JSONObject.parseArray(cache, c);
    }

    /************************************** String处理（实体转化为json字符串） End */

    /************************************** Object处理 */

    /**
     * 设置缓存没有过期时间
     *
     * @param key
     * @param o
     * @author micro-paul
     * @date 2022/3/16 10:39
     */
    public void setCache(String key, Object o) {
        setCache(key, JSONObject.toJSONString(o));
    }

    /**
     * 设置缓存有默认过期时间
     *
     * @param key
     * @param o
     * @author micro-paul
     * @date 2022/3/16 10:39
     */
    public void setCacheExpireDefault(String key, Object o) {
        if (StringUtils.isEmpty(key) || Objects.isNull(o)) {
            return;
        }
        valueOperations.set(key, JSONObject.toJSONString(o), DEFAULT_CACHE_TIME, TimeUnit.SECONDS);
    }

    /**
     * 设置缓存过期时间
     *
     * @param key
     * @param o
     * @param expire
     * @author micro-paul
     * @date 2022/3/16 10:43
     */
    public void setCacheExpire(String key, Object o, Long expire) {
        if (StringUtils.isEmpty(key) || Objects.isNull(o) || StringUtils.isEmpty(expire) || expire <= 0) {
            return;
        }
        valueOperations.set(key, JSONObject.toJSONString(o), expire, TimeUnit.SECONDS);
    }
    /************************************** Object处理 End */

    /************************************** List处理  */

    /**
     * 添加缓存
     *
     * @param key
     * @param list
     * @author micro-paul
     * @date 2022/3/16 11:20
     */
    public void rightPushAll(String key, List list) {
        if (StringUtils.isEmpty(key) || list.isEmpty()) {
            return;
        }
        List<String> redisList = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (Object obj : list) {
                if (obj != null) {
                    redisList.add(JSONObject.toJSONString(obj));
                }
            }
        }
        listOperations.rightPushAll(key, redisList);
    }

    /**
     * 添加缓存默认时间
     *
     * @param key
     * @param list
     * @author micro-paul
     * @date 2022/3/16 11:20
     */
    public void rightPushAllExpireDefault(String key, List list) {
        if (StringUtils.isEmpty(key) || list.isEmpty()) {
            return;
        }
        rightPushAll(key, list);
        setExpire(key, DEFAULT_CACHE_TIME);
    }

    /**
     * 添加缓存并设置过期时间
     *
     * @param key
     * @param list
     * @author micro-paul
     * @date 2022/3/16 11:20
     */
    public void rightPushAllExpire(String key, List list, Long time) {
        rightPushAll(key, list);
        setExpire(key, time);
    }

    /**
     * 添加缓存
     *
     * @param key
     * @param list
     * @author micro-paul
     * @date 2022/3/16 11:20
     */
    public void leftPushAll(String key, List list) {
        if (StringUtils.isEmpty(key) || list.isEmpty()) {
            return;
        }
        List<String> redisList = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (Object obj : list) {
                if (obj != null) {
                    redisList.add(JSONObject.toJSONString(obj));
                }
            }
        }
        listOperations.leftPushAll(key, redisList);
    }

    /**
     * 添加缓存默认时间
     *
     * @param key
     * @param list
     * @author micro-paul
     * @date 2022/3/16 11:20
     */
    public void leftPushAllExpireDefault(String key, List list) {
        if (StringUtils.isEmpty(key) || list.isEmpty()) {
            return;
        }
        leftPushAll(key, list);
        setExpire(key, DEFAULT_CACHE_TIME);
    }

    /**
     * 添加缓存并设置过期时间
     *
     * @param key
     * @param list
     * @author micro-paul
     * @date 2022/3/16 11:20
     */
    public void leftPushAllExpire(String key, List list, Long time) {
        leftPushAll(key, list);
        setExpire(key, time);
    }

    /**
     * 从缓存获取list数据
     *
     * @param key
     * @param c
     * @return java.util.List<T>
     * @author micro-paul
     * @date 2022/3/16 13:52
     */
    public <T> List<T> getRedisList(String key, Class<T> c) {
        List<String> list = listOperations.range(key, 0, -1);
        if (list != null && list.isEmpty()) {
            return null;
        }
        List<T> resultList = new ArrayList<>();
        for (String s : list) {
            resultList.add(JSONObject.parseObject(s, c));
        }
        return resultList;
    }

    /************************************** List处理 End */

    /************************************** Hash处理 */

    /**
     * 获取hash field 值
     *
     * @param key
     * @param field
     * @param c
     * @return T
     * @author micro-paul
     * @date 2022/3/16 13:58
     */
    public <T> T getHashVal(String key, String field, Class<T> c) {
        Object o = hashOperations.get(key, field);
        if (Objects.isNull(o)) {
            return null;
        }
        return JSONObject.parseObject(o.toString(), c);
    }

    /**
     * 获取hash field 值
     *
     * @param key
     * @param field
     * @param c
     * @return T
     * @author micro-paul
     * @date 2022/3/16 13:58
     */
    public <T> T getHashObject(String key, String field, Class<T> c) {
        Object o = hashOperations.get(key, field);
        if (Objects.isNull(o)) {
            return null;
        }
        return (T) o;
    }

    /**
     * 获取hash field 值
     *
     * @param key
     * @param field
     * @param c
     * @return T
     * @author micro-paul
     * @date 2022/3/16 13:58
     */
    public <T> List<T> getHashListVal(String key, String field, Class<T> c) {
        Object o = hashOperations.get(key, field);
        if (Objects.isNull(o)) {
            return null;
        }
        return JSONArray.parseArray(o.toString(), c);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param field
     * @param o
     * @param time
     * @author micro-paul
     * @date 2022/3/16 14:04
     */
    public void setHashValExpire(String key, String field, Object o, Long time) {
        hashOperations.put(key, field, JSONObject.toJSONString(o));
        setExpire(key, time);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param val
     * @param time
     * @author micro-paul
     * @date 2022/3/16 14:06
     */
    public void setHashValExpire(String key, HashMap val, Long time) {
        hashOperations.putAll(key, val);
        setExpire(key, time);
    }

    /**
     * 是否存在key的hash值
     *
     * @param key
     * @param field
     * @return boolean
     * @author micro-paul
     * @date 2022/3/16 14:06
     */
    public boolean hashHasKey(String key, String field) {
        return hashOperations.hasKey(key, field);
    }

    /**
     * Redis中有个INCR和INCRBY命令，都可以实现值递增的原子性操作，方便了解决了高并发时的冲突问题
     * ValueOperations中有increment方法，本质上是向Redis服务发送的INCRBY命令。当然要实现点击量递增的功能，需要使用ValueOperations的increment方法。
     * 以增量的形式改变集合存放的值。
     *
     * @param key
     * @param field
     * @param val
     * @author micro-paul
     * @date 2022/3/16 14:07
     */
    public void hashIncrement(String key, String field, Integer val) {
        this.hashOperations.increment(key, field, val);
    }

    /************************************** Hash处理 End */


    /************************************** zSet处理 */

    /**
     * 批量保存有序集合
     * 添加 tuples 到排序集 key ，或者 score 如果已存在则更新它。
     *
     * @param key
     * @param list
     * @author micro-paul
     * @date 2022/3/16 14:18
     */
    public void zSetAddExpireDefault(String key, List list) {
        if (StringUtils.isEmpty(key) || list.isEmpty()) {
            return;
        }
        Double index = 1d;
        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<>();
        for (Object o : list) {
            if (Objects.nonNull(o)) {
                String value = JSONObject.toJSONString(o);
                ZSetOperations.TypedTuple<String> typedTuple = new DefaultTypedTuple<>(value, index);
                tuples.add(typedTuple);
                index++;
            }
        }
        zSetOperations.add(key, tuples);
        setExpire(key, DEFAULT_CACHE_TIME);
    }

    /**
     * 范围查询，返回集合
     *
     * @param key
     * @param start
     * @param end
     * @param c
     * @return java.util.List<T>
     * @author micro-paul
     * @date 2022/3/16 14:32
     */
    public <T> List<T> zSetRange(String key, long start, long end, Class<T> c) {
        List<T> result = new ArrayList<>();
        Set<String> range = zSetOperations.range(key, start, end);
        if (range == null || range.isEmpty()) {
            return null;
        }
        for (String s : range) {
            if (!StringUtils.isEmpty(s)) {
                T t = JSONObject.parseObject(s, c);
                result.add(t);
            }
        }
        return result;
    }

    /**
     * 获取集合的长度
     *
     * @param key
     * @return java.lang.Long
     * @author micro-paul
     * @date 2022/3/16 14:37
     */
    public Long zSetGetSize(String key) {
        return zSetOperations.size(key);
    }

    /**
     * 分页获取数据
     *
     * @param key
     * @param c
     * @param pageNo
     * @param pageSize
     * @return java.util.List<T>
     * @author micro-paul
     * @date 2022/3/16 14:38
     */
    public <T> List<T> zSetRangeByPage(String key, Class<T> c, Integer pageNo, Integer pageSize) {
        long start = 0;
        if (pageNo > 1) {
            start = (pageNo - 1) * pageSize;
        }
        long end = pageNo * pageSize - 1;
        return zSetRange(key, start, end, c);
    }

    /************************************** zSet处理 End */
}
