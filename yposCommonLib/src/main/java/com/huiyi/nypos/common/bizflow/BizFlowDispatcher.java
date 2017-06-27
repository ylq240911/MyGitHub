package com.huiyi.nypos.common.bizflow;

import java.util.HashMap;
import java.util.Map;

import com.huiyi.nypos.common.uistep.IUIStep;

import android.text.TextUtils;


public class BizFlowDispatcher {
	
	private static final String NO_BUSINESS = "NO_BUSINESS";
	
	private Map<String, Class<? extends BaseBizFlow>> mapBizClass;
	private Map<String, BaseBizFlow> bizFlows;
	private BaseBizFlow curBizFlow = null;
	
	public BizFlowDispatcher(Class<? extends BaseBizFlow> noBussinessClass) {
		bizFlows = new HashMap<String, BaseBizFlow>();
		mapBizClass = new HashMap<String, Class<? extends BaseBizFlow>>();
		
		mapBizClass.put(NO_BUSINESS, noBussinessClass);
	}
	
	public void register(String bizType, Class<? extends BaseBizFlow> bizCls) {
		mapBizClass.put(bizType, bizCls);
	}
	
	public void unregister(String bizType) {
		mapBizClass.remove(bizType);
	}
	
	public void initBizFlow(BaseBizFlowContainer bizFlowContainer) {
		if (bizFlowContainer == null) {
			return;
		}
		
		curBizFlow.initContext(bizFlowContainer);
		curBizFlow.initBizFlow();
	}

	public void createBizFlow(String bizType) {
		if (TextUtils.isEmpty(bizType)) {
			bizType = NO_BUSINESS;
		}
		
		Class<? extends BaseBizFlow> bizCls = mapBizClass.get(bizType);
		if (bizCls != null) {
			try {
				curBizFlow = bizCls.newInstance();
				curBizFlow.setBizType(bizType);
			} catch (InstantiationException e) {
				e.printStackTrace();
				curBizFlow = createNoBizFlow();				
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				curBizFlow = createNoBizFlow();
			}
		} else {
			curBizFlow = createNoBizFlow();
		}
		bizFlows.put(bizType, curBizFlow);
	}
	
	private BaseBizFlow createNoBizFlow(){
		try {
			BaseBizFlow bizFlow=  mapBizClass.get(NO_BUSINESS).newInstance();
			bizFlow.setBizType(NO_BUSINESS);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public void loadBusiness(String bizType) {
		BaseBizFlow bizFlow = bizFlows.get(bizType);
		if (bizFlow == null) {
			//PosLogUtil.logi(TAG, "ERROR BUSINESS(" + bizType + ") not found.");
			return;
		}
		curBizFlow = bizFlow;
		//PosLogUtil.logi(TAG, "loadBusiness(" + bizType + ")");
	}
	
	public void finishBusiness(String bizType) {
		if (bizFlows.containsKey(bizType)) {
			bizFlows.remove(bizType);
			//PosLogUtil.logi(TAG, "finishBusiness(" + bizType + ")");
		}
	}

	public IUIStep[] getCurBizAllSteps() {
		if (curBizFlow != null) {
			return curBizFlow.getSteps();
		}
		return null;
	}
	
//	public void doAction(String actionName){
//		Log.i(TAG, "doAction");
//		if(curBizFlow!=null){
//			curBizFlow.doAction(actionName);
//		}
//	}
}
