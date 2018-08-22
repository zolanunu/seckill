-- ���ݿ��ʼ���ű�

-- �������ݿ�
-- create database seckill;
-- ʹ�����ݿ�

-- use seckill;

-- ������ɱ����
-- key: �������� 
drop table if exists seckill;
create table seckill (
	seckillId bigint not null auto_increment comment '��Ʒ���id',
	name varchar(120) not null comment '��Ʒ����',
	num int not null comment '�������',
	startTime timestamp not null default '0000-00-00 00:00:00' comment '��ɱ��ʼʱ��',
	endTime timestamp not null default '0000-00-00 00:00:00' comment '��ɱ����ʱ��',
	createTime timestamp not null default current_timestamp comment '����ʱ��',
	primary key (seckillId),
	key idx_startTime(startTime),
	key idx_endTime(endTime),
	key idx_create_time(createTime)
)engine = InnoDB auto_increment = 1000 default charset = utf8 comment = '��ɱ����';

-- ��ʼ������

insert into seckill(name, num, startTime, endTime)
values
('1000Ԫ��iphone6', 100, '2017-11-29 00:00:00', '2017-11-30 00:00:00'),
('500Ԫ������', 100, '2017-11-29 00:00:00', '2017-11-30 00:00:00'),
('100Ԫ��ipad', 100, '2017-11-29 00:00:00', '2017-11-30 00:00:00'),
('200Ԫ��hp�ʼǱ�', 100, '2017-11-29 00:00:00', '2017-11-30 00:00:00');

-- ��ɱ�ɹ���ϸ��
-- �û���½��֤��ص���Ϣ
drop table if exists success_killed;

create table success_killed (
	seckillId bigint not null comment '��ɱ��Ʒid',
	userPhone bigint not null comment '�û���½����',
	state tinyint not null default -1 comment '״̬��ʶ��-1 ��Ч, 0 �ɹ�, 1 �Ѹ���',
	createTime timestamp not null default '0000-00-00 00:00:00' comment '����ʱ��',
	primary key(seckillId, userPhone),/*������������ֹ�û���ͬһ����Ʒ�ظ���ɱ*/
	key idx_createTime(createTime)
)engine = InnoDB default charset=utf8 comment='��ɱ�ɹ���ϸ��';

-- ���ӿ���̨

-- mysql -u root -p
-- Ϊʲô��дDDL
-- ����������DAOʵ��ͱ��빤��