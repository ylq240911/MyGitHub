package com.huiyi.nypos.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.huiyi.nypos.common.log.LogStorageManager;

/**
 * 日志管理器
 * 
 * @author adam
 * 
 */
public class PosLogManager {

	private static String TAG = PosLogManager.class.getSimpleName();

	/**
	 * 自动模式
	 */
	public static final int EXPORT_TYPE_AUTO = 0;

	/**
	 * 手工模式
	 */
	public static final int EXPORT_TYPE_HAND = 1;

	private static LogStorageManager logStorageManager;

	public static void init(Context context) {
		if(logStorageManager==null){
			if (context == null)
				throw new IllegalArgumentException("context is null");
			logStorageManager = new LogStorageManager(context);
		}
	}

	/**
	 * 导出日志
	 * 
	 * @param logfilePath
	 *            导出文件路径
	 * @param exportType
	 *            0自动模式 1手动模式
	 * @param lastExportTime
	 *            最后一次导出时间 格式 yyyyMMddhhmmss
	 * @return
	 */
	public static void exportLog(String logFilePath, int exportType,
			String lastExportTime, OnLogExportFinishedListener listener)
			throws IllegalArgumentException {

		if (logFilePath == null || lastExportTime == null
				|| lastExportTime.length() != 14
				|| !isTimeFormat(lastExportTime))
			throw new IllegalArgumentException("exportLog args is unavailable");

		boolean fileInited = false; // 文件夹初始化是否成功

		// 文件存在先删除
		File logFile = new File(logFilePath);
		if (logFile.exists()) {
			fileInited = logFile.delete();
			Log.d(TAG, "logFile.delete:" + fileInited);
		} else {
			if (!logFile.getParentFile().exists()) {
				fileInited = logFile.getParentFile().mkdir();
				Log.d(TAG, "logFile.mkdir():" + fileInited);
			} else {
				fileInited = true;
			}
		}

		if (fileInited) {
			try {
				fileInited = logFile.createNewFile(); // 创建空文件
			} catch (Exception ex) {
				Log.e(TAG, "logFile.createNewFile error", ex);
				fileInited = false;
			}
		}

		if (!fileInited) {
			if (listener != null)
				listener.OnExportFinished(false, logFilePath, exportType,
						lastExportTime);
			return;
		}

		Cursor resultCursor = null;
		FileOutputStream fos = null;

		try {

			fos = new FileOutputStream(logFile);

			if (exportType == EXPORT_TYPE_HAND) {
				resultCursor = logStorageManager
						.queryForHandUploadRows(lastExportTime);

			} else {
				resultCursor = logStorageManager
						.queryForAutoUploadRows(lastExportTime);
			}

			StringBuilder sb = new StringBuilder();
			while (resultCursor.moveToNext()) {
				sb.delete(0, sb.length());
				sb.append(resultCursor.getString(1)).append(" "); // FIELD_CREATE_TIME
				sb.append(resultCursor.getString(2)).append(" "); // FIELD_PID
				sb.append(getPriorityDescr(resultCursor.getInt(3))).append(" "); // FIELD_PRIORITY
				sb.append(resultCursor.getString(4)).append(" "); // FIELD_PACKAGE_NAME
				sb.append(resultCursor.getString(5)).append(" "); // FIELD_TAG
				sb.append(resultCursor.getString(6)).append(" "); // FIELD_MSG
				if (resultCursor.getString(7) != null)
					sb.append("r1").append(resultCursor.getString(7))
							.append(" "); // FIELD_RESV1
				if (resultCursor.getString(8) != null)
					sb.append("r2").append(resultCursor.getString(8))
							.append(" "); // FIELD_RESV2
				if (resultCursor.getString(9) != null)
					sb.append("r3").append(resultCursor.getString(9))
							.append(" "); // FIELD_RESV3
				sb.append("\r\n");
				fos.write(sb.toString().getBytes());
				fos.flush();
			}
			if (listener != null)
				listener.OnExportFinished(true, logFilePath, exportType,
						lastExportTime);

		} catch (Exception ex) {
			PosLogger.e(TAG,ex.getMessage(),ex);
			if (listener != null)
				listener.OnExportFinished(false, logFilePath, exportType,
						lastExportTime);

		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
					Log.w(TAG, "fos.close exception", e);
				}
			if (resultCursor != null)
				resultCursor.close();
		}
	}

	/**
	 * 清除beforeWhatTime之前的日志
	 * 
	 * @param beforeWhatTime
	 *            格式 yyyyMMddhhmmss
	 * @return
	 */
	public static boolean clearLog(String beforeWhatTime) {

		if (beforeWhatTime.length() != 14 || !isTimeFormat(beforeWhatTime))
			throw new IllegalArgumentException("clearLog args is unavailable");
		try {
			int i = logStorageManager.deleteForHandUploadRows(beforeWhatTime);
			Log.d(TAG, "delete rows:" + i);
			return true;
		} catch (Exception ex) {
			Log.e(TAG, "delete fail", ex);
			return false;
		}

	}
	
	/**
	 * 清除所有日志
	 * @return
	 */
	public static boolean clearAllLog(){
		
		try {
			int i = logStorageManager.deleteForAllRows();

			Log.d(TAG, "delete rows:" + i);

			return true;

		} catch (Exception ex) {

			Log.e(TAG, "delete fail", ex);

			return false;
		}
	}

	/**
	 * 日志等级数字转成描述
	 * 
	 * @param priority
	 * @return
	 */
	private static String getPriorityDescr(int priority) {
		switch (priority) {
		case 2:
			return "VERBOSE";

		case 3:
			return "DEBUG";

		case 4:
			return "INFO";

		case 5:
			return "WARN";

		case 6:
			return "ERROR";

		default:
			return "";

		}
	}

	// 判断是否为日期格式yyyyMMddhhmmss
	private static boolean isTimeFormat(String time) {
		String strPattern = "\\d{14}";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(time);
		return m.matches();
	}
	
	//日志记录条数
	public static int getLogCount(){
		return logStorageManager.getLogCount();
	}

	/**
	 * 获取最近日志
	 * @param topRows
	 * @return
	 */
	public static String getLogs(int topRows){
		Cursor resultCursor = null;

		StringBuilder sb = new StringBuilder();
		
		try {

			resultCursor = logStorageManager
					.queryAllRows();

			int i=0;			
			
			while (resultCursor.moveToNext()) {
				sb.append(resultCursor.getString(1)).append(" "); // FIELD_CREATE_TIME
				sb.append(resultCursor.getString(2)).append(" "); // FIELD_PID
				sb.append(getPriorityDescr(resultCursor.getInt(3))).append(" "); // FIELD_PRIORITY
				sb.append(resultCursor.getString(4)).append(" "); // FIELD_PACKAGE_NAME
				sb.append(resultCursor.getString(5)).append(" "); // FIELD_TAG
				sb.append(resultCursor.getString(6)).append(" "); // FIELD_MSG
				if (resultCursor.getString(7) != null)
					sb.append("r1").append(resultCursor.getString(7))
							.append(" "); // FIELD_RESV1
				if (resultCursor.getString(8) != null)
					sb.append("r2").append(resultCursor.getString(8))
							.append(" "); // FIELD_RESV2
				if (resultCursor.getString(9) != null)
					sb.append("r3").append(resultCursor.getString(9))
							.append(" "); // FIELD_RESV3
				sb.append("\r\n");
				if(i++>topRows)
					break;
			}			

		} catch (Exception ex) {
			Log.e(TAG, "query log exception", ex);
			sb.append("获取日志失败:"+ex.getMessage());
			sb.append("\r\n");
			sb.append(ex.getStackTrace().toString());
		} finally {
			if (resultCursor != null)
				resultCursor.close();
		}
		
		return sb.toString();
	}
}
