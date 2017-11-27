package org.seckill.dao;
import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;


public interface SuccessKilledDao {
	//���빺����ϸ�������ظ���ɱ����
	int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
	
	// ����id��ѯSeccessKilled��Я����ɱ��Ʒ����ʵ��
	
	SuccessKilled queryByIdWIthSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
	// ���������ǻ���mybatisʵ��DAO
}