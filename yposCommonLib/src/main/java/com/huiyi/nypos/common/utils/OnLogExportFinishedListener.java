package com.huiyi.nypos.common.utils;

/**
 * 日志文件导出完成监听器
 * @author adam
 *
 */
public interface OnLogExportFinishedListener {
	/**
	 * 导出处理完成
	 * @param isSuccess  是否成功
	 * @param logFilePath  生成文件路径
	 * @param exportType 导出类型，和{@link #exportLog}相同
	 * @param lastExportTime 上次导出时间 yyyyMMddhhmmss，和{@link #exportLog}相同
	 */
	void OnExportFinished(boolean isSuccess, String logFilePath, int exportType, String lastExportTime);
}
