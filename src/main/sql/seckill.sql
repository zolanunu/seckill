-- ��ɱִ�д洢����
DELIMITER $$ --console ;ת��Ϊ$$
-- ����洢����
-- ������in���������
-- ������ out���������
-- row_count:������һ���޸�����sql��select���⣩��Ӱ������
-- row_count: 0:δ�޸�����; >0:��ʾ�޸ĵ�����; <0:sql����/δִ��
CREATE PROCEDURE 'seckill'.'execute_seckill' (in seckillid bigint,in phone bigint,in killtime timestamp,out r_result int)
	BEGIN
		DECLARE insert_count int DEFAULT 0;
		START TRANSACTION;
		insert ignore into success_killed (seckill_id, user_phone, create_time)
		values(v_seckill_id, v_phone, v_kill_time);
		select row_count() into insert_count;
		IF (insert_count = 0) THEN
			ROLLBACK;
			set r_result = -1;
		ELSIF(insert_count < 0) THEN
			ROLLBACK;
			set r_result = -2;
		ELSE
			UPDATE seckill set number = number - 1
			where seckill_id = seckillid
			and end_time > killtime
			and start_time < killtime
			and number > 0;
			select row_count() into insert_count;
			IF (insert_count = 0) THEN
				ROLLBACK;
				set r_result = 0;
			ELSIF(insert_count < 0) THEN
				ROLLBACK;
				set r_result = -2;
			ELSE
				COMMENT;
				set r_result = 1;
			END IF;
		END IF;
	END;
$$
-- �洢���̽���

DELIMITER ;
set @r_result = -3;
CALL execute_seckill(1003, 13502178891, now(), @r_result);
--��ȡ���
select @r_result;
		
	
	