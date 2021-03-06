package org.seckill.service;

import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

/**
 * *业务接口：站在使用者的角度去设计接口
 * * 三个方面：方法定义粒度，参数（越简练越好），返回类型（return 类型/异常）
* */
public interface SeckillService {
	/**
	 * 查询所有的秒杀记录
	 * */
	List<Seckill> getSeckillList();
	/**
	 * 查询单个秒杀记录
	 * */
	Seckill getById(long seckillId);
	
	/**
	 * 秒杀开启是输出秒杀接口地址，否则输出系统时间和秒杀时间
	 * */
	Exposer exportSeckillUrl(long seckillId);
	
	/**
	 * 执行秒杀操作
	 * */
	SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
	throws SeckillException, SeckillCloseException, RepeatKillException;
	/**
	 * 执行秒杀操作的by存储过程
	 * 某个商品被某个用户秒杀
	 * */
	SeckillExecution executeSeckillByProcedure(long seckillId, long userPhone, String md5);
}
