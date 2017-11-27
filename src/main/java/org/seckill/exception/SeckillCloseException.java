package org.seckill.exception;

/**
 * 秒杀关闭异常:秒杀关闭了，但是用户还是请求秒杀地址，这是异常，秒杀结束的原因有多个
 * */
public class SeckillCloseException extends SeckillException{

	public SeckillCloseException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public SeckillCloseException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
}
