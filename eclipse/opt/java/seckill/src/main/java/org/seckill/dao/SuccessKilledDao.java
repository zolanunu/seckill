package org.seckill.dao;
import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;


public interface SuccessKilledDao {
	//插入购买明细，过滤重复秒杀问题
	int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
	
	// 根据id查询SeccessKilled并携带秒杀产品对象实体
	
	SuccessKilled queryByIdWIthSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
	// 接下来就是基于mybatis实现DAO
}