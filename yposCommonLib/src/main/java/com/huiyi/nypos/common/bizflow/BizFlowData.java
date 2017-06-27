package com.huiyi.nypos.common.bizflow;

public class BizFlowData {	
	
	private String bizType; 
	
	private MemoryControl flowParams; //业务流程中传递的参数
	
	public BizFlowData(){
		flowParams=new MemoryControl();
	}
	
	public BizFlowData(MemoryControl flowParams){
		this.flowParams=flowParams;
	}
	/**
	 * @return the flowParams
	 */
	public MemoryControl getFlowParams() {
		return flowParams;
	}
	/**
	 * @param flowParams the flowParams to set
	 */
	public void setFlowParams(MemoryControl flowParams) {
		this.flowParams = flowParams;
	}

	/**
	 * @return the bizType
	 */
	public String getBizType() {
		return bizType;
	}

	/**
	 * @param bizType the bizType to set
	 */
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
}
