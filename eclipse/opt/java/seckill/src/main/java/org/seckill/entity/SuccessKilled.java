package org.seckill.entity;

import java.util.Date;

public class SuccessKilled {
	private long seckillId;
	private long userPhone;
	private short state;
	//private Date start_time;
	private Date create_time;
	public long getSeckillId() {
		return seckillId;
	}
	
	// 变通:多对一；成功秒杀一个单子，需要拿到实体
	// 在多方添加一方的信息
	// 接下来就是设计dao接口
	private Seckill seckill;
	
	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}
	public long getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(long userPhone) {
		this.userPhone = userPhone;
	}
	public short getState() {
		return state;
	}
	public void setState(short state) {
		this.state = state;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Seckill getSeckill() {
		return seckill;
	}
	public void setSeckill(Seckill seckill) {
		this.seckill = seckill;
	}
	
	
	
}
