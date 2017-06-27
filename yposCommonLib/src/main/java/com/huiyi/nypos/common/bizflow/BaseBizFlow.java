package com.huiyi.nypos.common.bizflow;

import java.util.ArrayList;

import com.huiyi.nypos.common.uistep.IUIStep;


/**
 * 业务流程基类
 * @author adam
 *
 */
public abstract class BaseBizFlow {
	
	private String bizType;
	
	//业务流程的页面展示的Activity容器
	private BaseBizFlowContainer baseContainer;
		
	/**
	 * 获取业务流程数据
	 * @return
	 */
	protected BizFlowData getBizFlowData(){
		return baseContainer.getBizFlowData();
	}
	
	protected void setBizFlowData(BizFlowData bizFlowData){
		baseContainer.setBizFlowData(bizFlowData);
	}
	
	/**
	 * 流程数据赋值
	 * @param key
	 * @param val
	 */
	protected void putFlowParam(String key,Object val){
		baseContainer.getBizFlowData().getFlowParams().setValue(key, val);
	}
	
	public void initContext(BaseBizFlowContainer baseContainer) {
		this.baseContainer = baseContainer;
	}


	/**
	 * @return the baseContainer
	 */
	public BaseBizFlowContainer getBaseContainer() {
		return baseContainer;
	}
		
	
	public IUIStep[] getSteps() {
		ArrayList<IUIStep> list = initSteps();
		if (list == null) {
			return null;
		}
		return list.toArray(new IUIStep[0]);
	}
	
	/**
	 * 初始化业务流程
	 */
	public abstract void initBizFlow();

	/**
	 * 初始化所有步骤step
	 * @return
	 */
	public abstract ArrayList<IUIStep> initSteps();
	
	/**
	 * 业务流程毁灭时
	 */
	public abstract void onDestroy();

	/**
	 * @return the bizType
	 */
	public String getBizType(){
		return bizType;
	}
	
	public void setBizType(String bizType){
		this.bizType= bizType;
	}
}
