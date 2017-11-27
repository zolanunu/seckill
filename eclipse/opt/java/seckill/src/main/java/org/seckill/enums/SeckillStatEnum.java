package org.seckill.enums;

public enum SeckillStatEnum {
	
	SUCCESS(1, "��ɱ�ɹ�"),
	END(0, "��ɱ����"),
	REPEAT_KILL(-1,"�ظ���ɱ"),
	INNER_ERROR(-2,"ϵͳ�쳣"),
	DATA_REWRITE(-3,"���ݴ۸�");
	
	private int state;
	private String stateInfo;
	
	SeckillStatEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getStateInfo() {
		return stateInfo;
	}
	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}
	
	public static SeckillStatEnum stateOf(int index) {
		for(SeckillStatEnum state : values()) {
			if(state.getState() == index) {
				return state;
			}
		}
		return null;
	}
}
