package org.seckill.dao;

import java.util.List;
import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

public interface SeckillDao {
	// ����� return Ӱ������> 1����ʾ���µļ�¼���� 
	int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);
	// ����id��ѯ��ɱ����
	Seckill queryById(long seckillId);
	// ����ƫ������ѯ��ɱ��Ʒ�б�
	List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);
	
}
