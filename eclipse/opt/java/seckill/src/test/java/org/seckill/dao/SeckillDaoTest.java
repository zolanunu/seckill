package org.seckill.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import javax.annotation.*;

import org.apache.ibatis.javassist.ClassPath;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Spring和junit整合，junit启动时候加载springIOC容器
 * spring-test,帮我们方便去做spring测试的依赖以及 junit依赖
 * */
@RunWith(SpringJUnit4ClassRunner.class) // 这是spring提供的一个接口，参数是
//加载以后还需要告诉junit spring配置文件

@ContextConfiguration({"classpath:spring/spring-dao.xml"})

public class SeckillDaoTest {
	// 配置文件告知以后，就开始注入Dao实现类依赖
	/* @SuppressWarnings("restriction")*/
	/* @Resource */
	// 去spring容器中找到SeckillDao这个类型的实现类
	// 注意这个Resource存在的位置：javax.annotation
	// 注入Dao依赖
	@Autowired
	private SeckillDao seckillDao;

	@Test
	public void testQueryById() throws Exception {
		long id = 1000;
		Seckill seckill = seckillDao.queryById(id);
		System.out.println(seckill.getName()+" "+seckill.getEndTime());
		System.out.println("测试成功");
		System.out.println(seckill);
	}
	@Test
	public void testQueryAll() throws Exception {
		/*eclipse如何进行junit对dao进行测试*/
		// java没有保存参数的记录：queryAll(int offset, int limit) -> query(arg0, arg1)
		List<Seckill> list = seckillDao.queryAll(0, 100);
		for(Seckill seckill : list) {
			System.out.println(seckill);
		}
		System.out.println("queryAll test success");
	}
	@Test
	public void testReduceNumber(){
		Date killTime = new Date();
		int updateCount = seckillDao.reduceNumber(1000L, killTime);
		System.out.println(updateCount);
		System.out.println("testReduceNumber success");
	}
	

}
