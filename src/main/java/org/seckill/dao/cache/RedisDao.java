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
	// ͨ��redisȥ�õ����ǵ�seckill���������Ͳ���Ҫȥ�������ǵ�db������ֱ��ȥ���ʵ����ǵ�redis
	
	public Seckill getSeckill(long seckillId) {
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				// ��û��ʵ���ڲ������л�
				// get->byte[]->�����л�->Object��Seckill��
				// �����Զ������л�
				// protostuff : pojo
				String key = "seckill:" + seckillId;
				byte[] bytes = jedis.get(key.getBytes());
				if(bytes!=null) {
					// �ն���
					Seckill seckill = schema.newMessage();
					// ѹ���ռ䣬ѹ���ٶ�
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
	
	// ��һ��seckill���󴫵ݵ����ǵ�redis�н��л���
	public String putSeckill(Seckill seckill) {
		// set Object(Seckill) -> ���л�һ����-��byte[]
		
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:"+seckill.getSeckillId();
				byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, 
						LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				int timeout = 60 * 60; // ����һ��Сʱ
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
