package org.seckill.dao.cache;

import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {
	private final Logger logger = LoggerFactory.getLogger(RedisDao.class);
	private JedisPool jedisPool;
	public RedisDao(String ip, int port) {
		jedisPool = new JedisPool(ip, port);
	}
	private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
	// 通过redis去拿到我们的seckill对象，这样就不需要去访问我们的db，而是直接去访问的我们的redis
	
	public Seckill getSeckill(long seckillId) {
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				// 并没有实现内部的序列化
				// get->byte[]->反序列化->Object（Seckill）
				// 采用自定义序列化
				// protostuff : pojo
				String key = "seckill:" + seckillId;
				byte[] bytes = jedis.get(key.getBytes());
				if(bytes!=null) {
					// 空对象
					Seckill seckill = schema.newMessage();
					// 压缩空间，压缩速度
					ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
					return seckill;
				}
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	// 把一个seckill对象传递到我们的redis中进行缓存
	public String putSeckill(Seckill seckill) {
		// set Object(Seckill) -> 序列化一数组-》byte[]
		
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:"+seckill.getSeckillId();
				byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, 
						LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				int timeout = 60 * 60; // 缓存一个小时
				String result = jedis.setex(key.getBytes(), timeout, bytes);
				return result;
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
