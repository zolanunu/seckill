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
 * ʹ��ע�����������ŵ㣺
 * 1: �����ŶӴ��һ�£���ȷ��ע���񷽷��ı�̷��
 * 2: ��֤���񷽷���ִ��ʱ�価���̣ܶ���Ҫ���������������RPC/HTTP������߰��뵽���񷽷��ⲿ
 * 
 * 3: �������еķ�������Ҫ�������ֻ��һ���޸Ĳ�����ֻ����������Ҫ�������
 * �����˽�mysql�м�����ص��ĵ�
 * 
 * */
// �������е����@Component

public class SeckillServiceImpl implements SeckillService {
	//�ռǶ���
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	//��Ҫ��spring������ע��service����������Ҫ�Լ�newһ��ʵ��
	@Autowired
	private SeckillDao seckillDao;
	@Autowired
	private SuccessKilledDao successkilledDao;
	//����һ�������ĸ��������ֵԽ����Խ�ã���������md5
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
 		// ϵͳʱ��
 		Date nowTime = new Date();
 		if(nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
 			return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
 		}
 		//ת���ض��ַ����Ĺ��̲�����
 		String md5 = getMd5(seckillId);
		return new Exposer(true, md5, seckillId);
	}
	private String getMd5(long seckillId) {
		// ����
		String baseString = seckillId + "/" + slat; //md5
		String md5 = DigestUtils.md5DigestAsHex(baseString.getBytes());
		return md5;
	}
	@Transactional
	public SeckillExecution executeSeckill(long seckillId, long userPhone,
			String md5) throws SeckillException, SeckillCloseException,
			RepeatKillException {
		// �ж��û���������md5�Ƿ�һ��
		if(md5 == null || !md5.equals(getMd5(seckillId))) {
			throw new SeckillException("seckill data rewrite");
		}
		// ִ����ɱ�߼��������+��¼������Ϊ
		Date nowTime = new Date();
		// �����
		int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
		try {
			if(updateCount <= 0) {
				// û�и��µ���¼, ��ɱ����
				throw new SeckillCloseException("seckill is closed");
			} else {
				// �����ɹ�����¼�������
				int insertCount = successkilledDao.insertSuccessKilled(seckillId, userPhone);
				// Ψһ
				if(insertCount <= 0) {
					// �ظ���ɱ
					throw new RepeatKillException("seckill repeated");
				} else {
					// ��ɱ�ɹ����ύ
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
			//���б����ڼ���쳣��ת��Ϊ�����ڼ���쳣
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
