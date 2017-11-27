package org.seckill.service;

import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

/**
 * *ҵ��ӿڣ�վ��ʹ���ߵĽǶ�ȥ��ƽӿ�
 * * �������棺�����������ȣ��������������ͣ�return ����/�쳣��
* */
public interface SeckillService {
	/**
	 * ��ѯ���е���ɱ��¼
	 * */
	List<Seckill> getSeckillList();
	/**
	 * ��ѯ������ɱ��¼
	 * */
	Seckill getById(long seckillId);
	
	/**
	 * ��ɱ�����������ɱ�ӿڵ�ַ���������ϵͳʱ�����ɱʱ��
	 * */
	Exposer exportSeckillUrl(long seckillId);
	
	/**
	 * ִ����ɱ����
	 * */
	SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
	throws SeckillException, SeckillCloseException, RepeatKillException;
	/**
	 * ִ����ɱ������by�洢����
	 * ĳ����Ʒ��ĳ���û���ɱ
	 * */
	SeckillExecution executeSeckillByProcedure(long seckillId, long userPhone, String md5);
}
