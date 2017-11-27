package org.seckill.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

@Service
@Transactional
/**
 * 使用注解控制事务的优点：
 * 1: 开发团队达成一致，明确标注事务方法的编程风格
 * 2: 保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部
 * 
 * 3: 不是所有的方法都需要事务，如果只有一条修改操作，只读才做不需要事务控制
 * 可以了解mysql行级锁相关的文档
 * 
 * */
// 代表所有的组件@Component

public class SeckillServiceImpl implements SeckillService {
	//日记对象
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	//需要从spring容器中注入service依赖，不需要自己new一个实例
	@Autowired
	private SeckillDao seckillDao;
	@Autowired
	private SuccessKilledDao successkilledDao;
	//加入一个混淆的概念，混淆的值越复杂越好，用来混淆md5
	private final String slat = "Yue@Fu@Za@Yue@Hao#$%^";
	
	public List<Seckill> getSeckillList() {
		
		return seckillDao.queryAll(0, 4);
	}

	public Seckill getById(long seckillId) {
		
		return seckillDao.queryById(seckillId);
	}
	
	/**
	 * exportSeckillUrl
	 * */
	public Exposer exportSeckillUrl(long seckillId) {
		
		Seckill seckill = seckillDao.queryById(seckillId);
		System.out.println(seckill.toString());
		if(seckill == null) {
			return new Exposer(false, seckillId);
		}
		Date startTime = seckill.getStartTime();
 		Date endTime = seckill.getEndTime();
 		// 系统时间
 		Date nowTime = new Date();
 		if(nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
 			return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
 		}
 		//转化特定字符串的过程不可逆
 		String md5 = getMd5(seckillId);
		return new Exposer(true, md5, seckillId);
	}
	private String getMd5(long seckillId) {
		// 加密
		String baseString = seckillId + "/" + slat; //md5
		String md5 = DigestUtils.md5DigestAsHex(baseString.getBytes());
		return md5;
	}
	@Transactional
	public SeckillExecution executeSeckill(long seckillId, long userPhone,
			String md5) throws SeckillException, SeckillCloseException,
			RepeatKillException {
		// 判断用户传过来的md5是否一致
		if(md5 == null || !md5.equals(getMd5(seckillId))) {
			throw new SeckillException("seckill data rewrite");
		}
		// 执行秒杀逻辑：减库存+记录购买行为
		Date nowTime = new Date();
		// 减库存
		int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
		try {
			if(updateCount <= 0) {
				// 没有更新到记录, 秒杀结束
				throw new SeckillCloseException("seckill is closed");
			} else {
				// 减库存成功，记录购买更新
				int insertCount = successkilledDao.insertSuccessKilled(seckillId, userPhone);
				// 唯一
				if(insertCount <= 0) {
					// 重复秒杀
					throw new RepeatKillException("seckill repeated");
				} else {
					// 秒杀成功，提交
					SuccessKilled successKilled = successkilledDao.queryByIdWIthSeckill(seckillId, userPhone);
					return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
				}
			}
		} catch(SeckillCloseException e1){
			throw e1;
		} catch(RepeatKillException e2){
			throw e2;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			//所有编译期间的异常，转化为运行期间的异常
			throw new SeckillException("seckill inner error: " + e.getMessage());
			
		}
	}

	public SeckillExecution executeSeckillByProcedure(long seckillId,
			long userPhone, String md5) {
		if(md5 == null || !md5.equals(getMd5(seckillId))) {
			return new SeckillExecution(seckillId, SeckillStatEnum.DATA_REWRITE);
		}
		Date killTime = new Date();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("seckillId", seckillId);
		map.put("userPhone", userPhone);
		map.put("killTime", killTime);
		map.put("result", null);
		try {
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
