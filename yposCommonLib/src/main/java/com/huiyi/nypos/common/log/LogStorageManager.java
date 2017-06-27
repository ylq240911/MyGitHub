package com.huiyi.nypos.common.log;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

/**
 * 日志存储管理
 * @author adam
 *
 */
public class LogStorageManager {
	
	private static String[] resultColumns = new String[] { LogDBConst.FIELD_ID, // 0
			LogDBConst.FIELD_CREATE_TIME, // 1
			LogDBConst.FIELD_PID, // 2
			LogDBConst.FIELD_PRIORITY, // 3
			LogDBConst.FIELD_PACKAGE_NAME, // 4
			LogDBConst.FIELD_TAG, // 5
			LogDBConst.FIELD_MSG, // 6
			LogDBConst.FIELD_RESV1, // 7
			LogDBConst.FIELD_RESV2, // 8
			LogDBConst.FIELD_RESV3 }; // 9
	
	private static final int MAX_ROWS = 5000; // 最大记录数

	private static final int RECORD_DAYS = 3; // 最大记录天数，每次满了MAX_ROWS后，清除该天数前的记录

	private static int currentRows = 0; // 当前记录数

	
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
		
	public LogStorageManager(Context context){
		logDBOpenHelper = new LogDBOpenHelper(context,
				LogDBOpenHelper.DATABASE_NAME, null,
				LogDBOpenHelper.DATABASE_VERSION);

		currentRows = getLogCount();
	}
	
	public Cursor queryById(String id){
		String where= LogDBConst.FIELD_ID + "=" + id;
		return query(where);
	}
	
	public Cursor queryForAutoUploadRows(String lastTime){
		String where= LogDBConst.FIELD_PRIORITY + ">=4 and "+ LogDBConst.FIELD_CREATE_TIME + ">'"
				+ lastTime + "'";
		return query(where);
	}
	
	public Cursor queryForHandUploadRows(String lastTime){
		String where= LogDBConst.FIELD_CREATE_TIME + ">'" + lastTime + "'";
		return query(where);
	}
	
	public Cursor queryAllRows(){
		return query(null);
	}

	
	private Cursor query(String where) {		
		
		SQLiteDatabase db = logDBOpenHelper.getReadableDatabase();

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(LogDBOpenHelper.DATABASE_TABLE);

		if(where!=null){
			queryBuilder.appendWhere(where);
		}
		String sortOrder = "create_time desc";
		
		Cursor cursor = queryBuilder.query(db, resultColumns,null,null,null,null, sortOrder);
		return cursor;

	}
	
	public int deleteForHandUploadRows(String lastTime) {

		String where = LogDBConst.FIELD_CREATE_TIME + "<='" + lastTime + "'";
		
		return delete(where);
	}
	
	public int deleteForAllRows(){
		return delete(null);
	}
	
	public int deleteForAutoUploadRows(String lastTime) {

		String where = LogDBConst.FIELD_PRIORITY + ">=5 and "
					+ LogDBConst.FIELD_CREATE_TIME + "<='" + lastTime + "'";
		
		return delete(where);
	}
	
	
	private int delete(String where) {
		SQLiteDatabase db = logDBOpenHelper.getWritableDatabase();

		int deleteCount = db.delete(LogDBOpenHelper.DATABASE_TABLE, where,
				null);		

		currentRows = getLogCount();

		return deleteCount;
	}
	
		
	// 统计记录总条数
	public int getLogCount() {

		SQLiteDatabase db = logDBOpenHelper.getWritableDatabase();

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(LogDBOpenHelper.DATABASE_TABLE);
		Cursor cr = queryBuilder.query(db, new String[] { "count(1)" }, null,
				null, null, null, null);
		if (cr.moveToNext()) {
			return cr.getInt(0);
		} else {
			return 0;
		}
	}
	
	/**
	 * 检查是否满了，满后了做清理
	 */
	private void checkIsFullAndDelete() {

		if (currentRows >= MAX_ROWS) {

			deleteLogBeforeDays(RECORD_DAYS);

			currentRows = getLogCount();
		}
	}
	
	/**
	 * 插入一条记录
	 * @param values
	 * @return
	 */
	public long insert(ContentValues values) {

		SQLiteDatabase db = logDBOpenHelper.getWritableDatabase();

		long id = db.insert(LogDBOpenHelper.DATABASE_TABLE, null,
				values);

		if (id > -1) {

			currentRows++;

			checkIsFullAndDelete();
		}
		
		return id;
	}
	
	/**
	 * 删除 day 天前的日志
	 * 
	 * @param day
	 */
	public void deleteLogBeforeDays(int day) {

		Date now = new Date();
		long m = now.getTime() - day * 24 * 60 * 60 * 1000;

		Date before = new Date(m);

		String time = formatter.format(before);

		StringBuffer deleteSqlBuffer = new StringBuffer();
		deleteSqlBuffer = deleteSqlBuffer.append("DELETE FROM ")
				.append(LogDBOpenHelper.DATABASE_TABLE).append(" WHERE ")
				.append(LogDBConst.FIELD_CREATE_TIME).append(" < '")
				.append(time).append("'");

		SQLiteDatabase db = logDBOpenHelper.getWritableDatabase();

		db.execSQL(deleteSqlBuffer.toString());

	}
	
	private LogDBOpenHelper logDBOpenHelper;
	
	private class LogDBOpenHelper extends SQLiteOpenHelper {

		private static final String TAG2 = "LogDBOpenHelper";

		private static final String DATABASE_NAME = "ypos/yposlogdat.db";
		private static final String DATABASE_TABLE = "LogInfos";
		private static final int DATABASE_VERSION = 2;

		private static final String DATABASE_CREATE = "CREATE TABLE "
				+ DATABASE_TABLE + " (" + LogDBConst.FIELD_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + LogDBConst.FIELD_PID
				+ " TEXT, " + LogDBConst.FIELD_TID + " TEXT, "
				+ LogDBConst.FIELD_PRIORITY + " INTEGER, "
				+ LogDBConst.FIELD_TAG + " TEXT, " + LogDBConst.FIELD_MSG
				+ " TEXT, " + LogDBConst.FIELD_PACKAGE_NAME + " TEXT, "
				+ LogDBConst.FIELD_RESV1 + " TEXT, " + LogDBConst.FIELD_RESV2
				+ " TEXT, " + LogDBConst.FIELD_RESV3 + " TEXT, "
				+ LogDBConst.FIELD_CREATE_TIME + " TEXT ); ";

		public LogDBOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(new LogDBContextWrapper(context), name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			Log.w(TAG2, "Upgrading from version " + oldVersion + " to "
					+ newVersion + ", which will destory all old data");

			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

			onCreate(db);
		}
	}
}