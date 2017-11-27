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
 * Spring��junit���ϣ�junit����ʱ�����springIOC����
 * spring-test,�����Ƿ���ȥ��spring���Ե������Լ� junit����
 * */
@RunWith(SpringJUnit4ClassRunner.class) // ����spring�ṩ��һ���ӿڣ�������
//�����Ժ���Ҫ����junit spring�����ļ�

@ContextConfiguration({"classpath:spring/spring-dao.xml"})

public class SeckillDaoTest {
	// �����ļ���֪�Ժ󣬾Ϳ�ʼע��Daoʵ��������
	/* @SuppressWarnings("restriction")*/
	/* @Resource */
	// ȥspring�������ҵ�SeckillDao������͵�ʵ����
	// ע�����Resource���ڵ�λ�ã�javax.annotation
	// ע��Dao����
	@Autowired
	private SeckillDao seckillDao;

	@Test
	public void testQueryById() throws Exception {
		long id = 1000;
		Seckill seckill = seckillDao.queryById(id);
		System.out.println(seckill.getName()+" "+seckill.getEndTime());
		System.out.println("���Գɹ�");
		System.out.println(seckill);
	}
	@Test
	public void testQueryAll() throws Exception {
		/*eclipse��ν���junit��dao���в���*/
		// javaû�б�������ļ�¼��queryAll(int offset, int limit) -> query(arg0, arg1)
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
