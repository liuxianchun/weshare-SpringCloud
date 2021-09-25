package com.lxc.common.utils;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author: liuxianchun
 * @Date: 2021/05/24
 * @Description: <String,Object>操作redis
 */

public class RedisUtil {

    private Log log = LogFactory.getCurrentLogFactory().getLog(RedisUtil.class);

    public RedisUtil() {
    }

    private RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

    public void setRedisTemplate(RedisTemplate<String,Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public boolean expire(String key, long time) {
        try {
            if (time > 0L) {
                this.redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }


    public Long getExpire(String key) {
        return this.redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public boolean setNX(String key,Object value,Long time) {
        try {
            Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
            if (result != null) {
                return result.booleanValue();
            }
        } catch (Throwable e) {
            log.error("setNX error", e);
        }
        return false;
    }


    public Boolean hasKey(String key) {
        try {
            return this.redisTemplate.hasKey(key);
        } catch (Exception e) {
            return false;
        }
    }


    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                this.redisTemplate.delete(key[0]);
            } else {
                this.redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }


    public Object get(String key) {
        return key == null ? null : this.redisTemplate.opsForValue().get(key);
    }


    public boolean set(String key, Object value) {
        try {
            this.redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0L)
                this.redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            else
                this.set(key, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public Long incr(String key, long delta) {
        if (delta < 0L) {
            throw new RuntimeException("递增因子必须大于0");
        } else
            return this.redisTemplate.opsForValue().increment(key, delta);
    }

    public Long incr(String key, long delta,long ttl) {
        if (delta < 0L) {
            throw new RuntimeException("递增因子必须大于0");
        } else{
            Long increment = this.redisTemplate.opsForValue().increment(key, delta);
            this.redisTemplate.expire(key,ttl,TimeUnit.SECONDS);
            return increment;
        }
    }


    public Long decr(String key, long delta) {
        if (delta < 0L) {
            throw new RuntimeException("递增因子必须大于0");
        } else
            return this.redisTemplate.opsForValue().increment(key, -delta);
    }

    public Long decr(String key, long delta,long ttl) {
        if (delta < 0L) {
            throw new RuntimeException("递增因子必须大于0");
        } else{
            Long increment = this.redisTemplate.opsForValue().increment(key, -delta);
            this.redisTemplate.expire(key,ttl,TimeUnit.SECONDS);
            return increment;
        }
    }

    /**
     * 实现命令：KEYS pattern，查找所有符合给定模式 pattern的 key
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }


    public Object hget(String key, String item) {
        return this.redisTemplate.opsForHash().get(key, item);
    }


    public Map<Object, Object> hmget(String key) {
        return this.redisTemplate.opsForHash().entries(key);
    }


    public boolean hmset(String key, Map<String, Object> map) {
        try {
            this.redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            this.redisTemplate.opsForHash().putAll(key, map);
            if (time > 0L) {
                this.expire(key, time);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public boolean hset(String key, String item, Object value) {
        try {
            this.redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public boolean hset(String key, String item, Object value, long time) {
        try {
            this.redisTemplate.opsForHash().put(key, item, value);
            if (time > 0L)
                this.expire(key, time);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public void hdel(String key, Object... item) {
        this.redisTemplate.opsForHash().delete(key, item);
    }


    public boolean hHasKey(String key, String item) {
        return this.redisTemplate.opsForHash().hasKey(key, item);
    }


    public double hincr(String key, String item, double by) {
        return this.redisTemplate.opsForHash().increment(key, item, by);
    }


    public double hdecr(String key, String item, double by) {
        return this.redisTemplate.opsForHash().increment(key, item, -by);
    }


    public Set<Object> sGet(String key) {
        try {
            return this.redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            return null;
        }
    }


    public Boolean sHasKey(String key, Object value) {
        try {
            return this.redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            return false;
        }
    }


    public Long sSet(String key, Object... values) {
        try {
            return this.redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            return 0L;
        }
    }

    public Long sDel(String key, Object... values) {
        try {
            return this.redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            return 0L;
        }
    }


    public Long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = this.redisTemplate.opsForSet().add(key, values);
            if (time > 0L) {
                this.expire(key, time);
            }
            return count;
        } catch (Exception e) {
            return 0L;
        }
    }


    public Long sGetSetSize(String key) {
        try {
            return this.redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            return 0L;
        }
    }


    public List<Object> lGet(String key, long start, long end) {
        try {
            return this.redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            return null;
        }
    }


    public Long lGetListSize(String key) {
        try {
            return this.redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            return 0L;
        }
    }


    public Object lGetIndex(String key, long index) {
        try {
            return this.redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            return 0L;
        }
    }

    public boolean lSet(String key, Object value) {
        try {
            this.redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean lSet(String key, Object value, long time) {
        try {
            this.redisTemplate.opsForList().rightPush(key, value);
            if (time > 0L) {
                this.expire(key, time);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean lSet(String key, List<Object> value) {
        try {
            this.redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean lSet(String key, List<Object> value, long time) {
        try {
            this.redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0L) {
                this.expire(key, time);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            this.redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public Long lRemove(String key, long count, Object value) {
        try {
            Long remove = this.redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            return 0L;
        }
    }
}
