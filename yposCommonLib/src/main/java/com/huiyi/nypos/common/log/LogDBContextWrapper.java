package com.huiyi.nypos.common.log;

import java.io.File;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

/**
 * log db 自定义contextwrapper
 * 
 * @author adam
 * 
 */
public class LogDBContextWrapper extends ContextWrapper {

	public LogDBContextWrapper(Context base) {
		super(base);
	}

	@Override
	public File getDatabasePath(String name) {
		File sdcardDir = Environment.getExternalStorageDirectory();
		String dbfile = sdcardDir.getAbsolutePath() + "/"+ name;
		if (!dbfile.endsWith(".db")) {
			dbfile += ".db";
		}
		
		Log.w("LogDBContextWrapper", "dbfile:"+dbfile);
		File result = new File(dbfile);

		if (!result.getParentFile().exists()) {
			result.getParentFile().mkdirs();
		}

		return result;
	}

	/*
	 * this version is called for android devices >= api-11. thank to @damccull
	 * for fixing this.
	 */
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			SQLiteDatabase.CursorFactory factory,
			DatabaseErrorHandler errorHandler) {
		return openOrCreateDatabase(name, mode, factory);
	}

	/* this version is called for android devices < api-11 */
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			SQLiteDatabase.CursorFactory factory) {
		SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(
				getDatabasePath(name), null);

		return result;
	}

}
