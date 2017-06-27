package com.huiyi.nypos.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.huiyi.nypos.common.log.LogDBConst;
import com.huiyi.nypos.common.log.LogStorageManager;

import android.content.ContentValues;
import android.content.Context;
import android.os.Process;
import android.util.Log;

/**
 * 日志
 * 
 * @author adam
 * 
 */
public class PosLogger {

	/**
	 * save all Priority .
	 */
	public static final int ALL = 0;

	/**
	 * logSavePriority constant for save; use PosLogger.v.
	 */
	public static final int VERBOSE = 2;

	/**
	 * logSavePriority constant for save; use PosLogger.d.
	 */
	public static final int DEBUG = 3;

	/**
	 * logSavePriority constant for save; use PosLogger.i.
	 */
	public static final int INFO = 4;

	/**
	 * logSavePriority constant for save; use PosLogger.w.
	 */
	public static final int WARN = 5;

	/**
	 * logSavePriority constant for save; use PosLogger.e.
	 */
	public static final int ERROR = 6;

	/**
	 * logSavePriority constant for save ; .
	 */
	public static final int NOSAVE = 99;

	private static SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	private static int logSavePriority;

	private static LogStorageManager logStorageManager;
	
	private static String appPackageName;
	
	private PosLogger() {
	}

	/**
	 * 获取logger
	 * 
	 * @param appContext
	 * @param cls
	 * @param logSavePriority
	 *            默认为INFO 4 只有INFO级的才上送到服务器
	 * @return 
	 * @return
	 */
	public static void init(Context context, int savePriority) {
		if(logStorageManager==null){
			if (context == null)
				throw new IllegalArgumentException("context is null");
			
			appPackageName=context.getPackageName();
			
			logStorageManager =new LogStorageManager(context);	
		}
		
		logSavePriority = savePriority;
	}

	/**
	 * Send a {@link #VERBOSE} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void v(String tag, String msg) {
		v(tag, msg, false);
	}

	public static void v(String tag, String msg, boolean onlyLocal) {
		Log.v(tag, msg);
		if (!onlyLocal && logSavePriority <= VERBOSE) {
			saveLog(VERBOSE, tag, msg);
		}
	}

	/**
	 * Send an {@link #INFO} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void i(String tag, String msg) {
		i(tag, msg, false);
	}

	/**
	 * Send an {@link #INFO} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param onlyLocal
	 *            is only by andrioid log ,not save
	 */
	public static void i(String tag, String msg, boolean onlyLocal) {
		Log.i(tag, msg);
		if (!onlyLocal && logSavePriority <= INFO) {
			saveLog(INFO, tag, msg);
		}
	}

	/**
	 * Send a {@link #DEBUG} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param onlyLocal
	 *            is only by andrioid log ,not save
	 */
	public static void d(String tag, String msg) {
		d(tag, msg, false);
	}

	/**
	 * Send a {@link #DEBUG} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param onlyLocal
	 *            is only by andrioid log ,not save
	 */
	public static void d(String tag, String msg, boolean onlyLocal) {
		Log.d(tag, msg);
		if (!onlyLocal && logSavePriority <= DEBUG) {
			saveLog(DEBUG, tag, msg);
		}
	}

	/**
	 * Send a {@link #WARN} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void w(String tag, String msg) {
		w(tag, msg, false);
	}

	/**
	 * Send a {@link #WARN} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param onlyLocal
	 *            is only by andrioid log ,not save
	 */
	public static void w(String tag, String msg, boolean onlyLocal) {
		Log.w(tag, msg);
		if (!onlyLocal && logSavePriority <= WARN) {
			saveLog(WARN, tag, msg);
		}
	}

	/**
	 * Send a {@link #WARN} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param thr
	 *            An exception to log
	 */
	public static void w(String tag, String msg, Throwable thr) {
		w(tag, msg, false);
	}

	/**
	 * Send a {@link #WARN} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param thr
	 *            An exception to log
	 * @param onlyLocal
	 *            is only by andrioid log ,not save
	 */
	public static void w(String tag, String msg, Throwable thr, boolean onlyLocal) {
		Log.w(tag, msg);
		if (!onlyLocal && logSavePriority <= WARN) {
			saveLog(WARN, tag, msg);
		}
	}

	/**
	 * Send a {@link #ERROR} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 */
	public static void e(String tag, String msg, Throwable thr) {
		e(tag, msg, thr, false);
	}

	/**
	 * Send a {@link #ERROR} log message and log the exception.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param tr
	 *            An exception to log
	 * @param onlyLocal
	 *            is only by andrioid log ,not save
	 */
	public static void e(String tag, String msg, Throwable thr, boolean onlyLocal) {
		Log.e(tag, msg, thr);
		if (!onlyLocal && logSavePriority <= ERROR) {
			saveLog(ERROR, tag, msg, thr);
		}
	}

	/**
	 * Send an {@link #ERROR} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 */
	public static void e(String tag, String msg) {
		e(tag, msg, false);
	}

	/**
	 * Send an {@link #ERROR} log message.
	 * 
	 * @param tag
	 *            Used to identify the source of a log message. It usually
	 *            identifies the class or activity where the log call occurs.
	 * @param msg
	 *            The message you would like logged.
	 * @param onlyLocal
	 *            is only by andrioid log ,not save
	 */
	public static void e(String tag, String msg, boolean onlyLocal) {
		Log.e(tag, msg);
		if (!onlyLocal && logSavePriority <= ERROR) {
			saveLog(ERROR, tag, msg);
		}
	}

	private static void saveLog(int priority, String tag, String msg, Throwable thr) {
		saveLog(priority, tag, msg + '\n' + Log.getStackTraceString(thr));
	}

	private static void saveLog(int priority, String tag, String msg) {
		
		try {			
			ContentValues cv = new ContentValues();
			
			cv.clear();
			cv.put(LogDBConst.FIELD_PID, Process.myPid());
			cv.put(LogDBConst.FIELD_TID, Process.myTid());
			cv.put(LogDBConst.FIELD_PACKAGE_NAME, appPackageName);
			cv.put(LogDBConst.FIELD_PRIORITY, priority);
			cv.put(LogDBConst.FIELD_TAG, tag);
			cv.put(LogDBConst.FIELD_MSG, msg);
			cv.put(LogDBConst.FIELD_CREATE_TIME,
					formatter.format(new Date()));
			logStorageManager.insert(cv);			
		} catch (Exception ex) {
			Log.e("PosLogger", "saveLog异常", ex);
		}

	}

}
