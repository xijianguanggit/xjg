package com.tedu.base.common.redis;

import java.util.List;

public interface RedisService {
	public boolean add(String keyStr, String value) throws KeyNotFindException;
	public void delete(List<String> keys);
	public void delete(String keys);
	public boolean update(String keyStr, String value) throws KeyNotFindException;
	public String get(final String keyId);
    public boolean set(final String key, Object value, Long expireTime);
}
