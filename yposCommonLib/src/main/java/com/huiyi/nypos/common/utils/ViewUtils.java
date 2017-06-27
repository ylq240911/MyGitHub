package com.huiyi.nypos.common.utils;

import com.huiyi.nypos.common.utils.PosLogger;

public class ViewUtils {
	public static long lastClickTime;
    public synchronized static boolean isFastClick(long curTime) {
        long time = System.currentTimeMillis();  
        long  fastTime = time - lastClickTime;
        PosLogger.i("isFastClick", fastTime+"");
        if ( fastTime < curTime) {  
            return true;   
        }   
        lastClickTime = time;   
        return false;   
    }
}
