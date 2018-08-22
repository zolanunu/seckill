-- 数据库初始化脚本

-- 创建数据库
-- create database seckill;
-- 使用数据库

-- use seckill;

-- 创建秒杀库存表
-- key: 建立索引 
drop table if exists seckill;
create table seckill (
	seckillId bigint not null auto_increment comment '商品库存id',
	name varchar(120) not null comment '商品名称',
	num int not null comment '库存数量',
	startTime timestamp not null default '0000-00-00 00:00:00' comment '秒杀开始时间',
	endTime timestamp not null default '0000-00-00 00:00:00' comment '秒杀结束时间',
	createTime timestamp not null default current_timestamp comment '创建时间',
	primary key (seckillId),
	key idx_startTime(startTime),
	key idx_endTime(endTime),
	key idx_create_time(createTime)
)engine = InnoDB auto_increment = 1000 default charset = utf8 comment = '秒杀库存表';

-- 初始化数据

insert into seckill(name, num, startTime, endTime)
values
('1000元秒iphone6', 100, '2017-11-29 00:00:00', '2017-11-30 00:00:00'),
('500元秒魅族', 100, '2017-11-29 00:00:00', '2017-11-30 00:00:00'),
('100元秒ipad', 100, '2017-11-29 00:00:00', '2017-11-30 00:00:00'),
('200元秒hp笔记本', 100, '2017-11-29 00:00:00', '2017-11-30 00:00:00');

-- 秒杀成功明细表
-- 用户登陆认证相关的信息
drop table if exists success_killed;

create table success_killed (
	seckillId bigint not null comment '秒杀商品id',
	userPhone bigint not null comment '用户登陆号码',
	state tinyint not null default -1 comment '状态标识：-1 无效, 0 成功, 1 已付款',
	createTime timestamp not null default '0000-00-00 00:00:00' comment '创建时间',
	primary key(seckillId, userPhone),/*联合主键，防止用户对同一个商品重复秒杀*/
	key idx_createTime(createTime)
)engine = InnoDB default charset=utf8 comment='秒杀成功明细表';

-- 连接控制台

-- mysql -u root -p
-- 为什么手写DDL
-- 接下来就是DAO实体和编码工作