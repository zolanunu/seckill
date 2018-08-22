package org.seckill.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"classpath:spring/spring-dao.xml",
	"classpath:spring/spring-service.xml"
})
public class SeckillServiceTest {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	// 没有配置，slg4j只是接口，但是配置是logback，去logback官网查找配置
	@Autowired
	private SeckillService seckillService;
	@Test
	public void testGetSeckillList() throws Exception {
		List<Seckill> list = seckillService.getSeckillList();
		logger.info("list={} ", list);
		System.out.println("testGetSeckillList success");
	}

	@Test
	public void testGetById() throws Exception {
		long id = 1000;
		Seckill seckill = seckillService.getById(id);
		logger.info("seckill={} ", seckill);
		System.out.println("testGetById success");
	}

	@Test
	public void testExportSeckillUrl() throws Exception {
		long id = 1004L;
		Exposer exposer = seckillService.exportSeckillUrl(id);
		logger.info("exposer={} ", exposer);
		System.out.println("exposer: "+exposer.toString());
		System.out.println("testExportSeckillUrl success");
		// exposer: Exposer [exposed=true, 
		// md5=1843fecb26798d7f4afe2d0b4b906ff6, seckillId=1004, now=0, startTime=0, endTime=0]
	}

	@Test
	public void testExecuteSeckill() {
		long id = 1004L;
		long userPhone = 13502171120L;
		
		String md5 = "1843fecb26798d7f4afe2d0b4b906ff6";
		// 会出现重复秒杀的异常
		try {
			SeckillExecution execution = seckillService.executeSeckill(id, userPhone, md5);
			logger.info("result = {}: ", execution);
			System.out.println("执行秒杀成功测试");
		} catch (RepeatKillException e) {
			logger.error(e.getMessage());
		} catch (SeckillCloseException e1) {
			logger.error(e1.getMessage());
		}
		
	}

	@Test
	public void testExecuteSeckillByProcedure() {
		long id = 1004;
		long userPhone = 13502171120L;
		Exposer exposer = seckillService.exportSeckillUrl(id);
		if(exposer.isExposed()) {
			String md5 = exposer.getMd5();
			SeckillExecution execution = seckillService.executeSeckillByProcedure(id, userPhone, md5);
			logger.info(execution.getStateInfo());
		}
	}
	@Test
	public void testSeckillLogic() throws Exception {
		long id = 1001;
		Exposer exposer = seckillService.exportSeckillUrl(id);
		if(exposer.isExposed()) {
			logger.info("exposer = {}", exposer);
			long phone = 13502171127L;
			String md5 = exposer.getMd5();
			try {
				SeckillExecution execution = seckillService.executeSeckill(id, phone, md5);
				logger.info("result = {}", execution);
			} catch (RepeatKillException e) {
				logger.error(e.getMessage());
			} catch (SeckillCloseException e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("秒杀未开启");
		}
	}
}
