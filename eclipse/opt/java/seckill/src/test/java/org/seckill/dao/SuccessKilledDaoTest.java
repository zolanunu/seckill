package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})//����JUnit spring�����ļ�����
public class SuccessKilledDaoTest {
	
	@Autowired
	SuccessKilledDao successKilledDao;
	
	@Test
	public void testInsertSuccessKilled(){
		int insertCount = successKilledDao.insertSuccessKilled(1001, 15764210366L);
		System.out.println(insertCount);
		System.out.println("testInsertSuccessKilled");
	}
	
	@Test
	public void testQueryByIdWithSeckill(){
		SuccessKilled successKilled = successKilledDao.queryByIdWIthSeckill(1000, 15764210366L);
		System.out.println(successKilled);
		System.out.println(successKilled.getSeckill());
		System.out.println("testQueryByIdWithSeckill");
	}

}
