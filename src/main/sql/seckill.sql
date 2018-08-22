-- 秒杀执行存储过程
DELIMITER $$ --console ;转换为$$
-- 定义存储过程
-- 参数：in：输入参数
-- 参数： out：输出参数
-- row_count:返回上一条修改类型sql（select除外）的影响行数
-- row_count: 0:未修改数据; >0:表示修改的行数; <0:sql错误/未执行
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
-- 存储过程结束

DELIMITER ;
set @r_result = -3;
CALL execute_seckill(1003, 13502178891, now(), @r_result);
--获取结果
select @r_result;
		
	
	