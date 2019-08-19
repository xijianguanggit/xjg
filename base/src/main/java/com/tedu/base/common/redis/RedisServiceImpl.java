package com.tedu.base.common.redis;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import com.tedu.base.engine.util.FormLogger;

@Service("redisService")
public class RedisServiceImpl extends RedisGeneratorDao<String, String> implements RedisService {
	/**
     * 写入缓存
     * 
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(key, value.toString());
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            FormLogger.logConf(e.getMessage(), FormLogger.LOG_TYPE_ERROR);
        }
        return result;
    }
    
    
	/**
	 * 添加
	 * @throws Exception 
	 */
	public boolean add(String keyStr, String value) throws KeyNotFindException {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(keyStr);
				byte[] name = serializer.serialize(value);
				return connection.setNX(key, name);
			}
		});
		if (!result) {
			throw new KeyNotFindException("添加失败，可能原因是key已经存在, key = " + keyStr);
		}
		return result;
	}

	/**
	 * 删除
	 */
	public void delete(List<String> keys) {
		redisTemplate.delete(keys);
	}

	/**
	 * 删除
	 */
	public void delete(String keys) {
		redisTemplate.delete(keys);
	}

	/**
	 * 修改对象
	 * @throws Exception 
	 */
	public boolean update(String keyStr, String value) throws KeyNotFindException {
		if (get(keyStr) == null) {
			throw new KeyNotFindException("数据行不存在, key = " + keyStr);
		}
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(keyStr);
				byte[] name = serializer.serialize(value);
				connection.set(key, name);
				return true;
			}
		});
		return result;
	}

	public String get(final String keyId) {
		String result = redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(keyId);
				byte[] value = connection.get(key);
				if (value == null) {
					return null;
				}
				return serializer.deserialize(value);
			}
		});
		return result;
	}

}
