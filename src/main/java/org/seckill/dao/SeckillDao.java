package org.seckill.dao;

import java.util.List;
import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

public interface SeckillDao {
	// 减库存 return 影响行数> 1，表示更新的记录行数 
	int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);
	// 根据id查询秒杀对象
	Seckill queryById(long seckillId);
	// 根据偏移量查询秒杀商品列表
	List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);
	
}
