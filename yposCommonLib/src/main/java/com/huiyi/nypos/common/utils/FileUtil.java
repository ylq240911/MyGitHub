package com.huiyi.nypos.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

/**
 * @description 文件夹工具类
 */
public class FileUtil {

	private final static String TAG = "FileUtil";

	public static String SDPATH;
	public final static String DOWNLOADPATH = "/download"; // 下载的数据，放置于sd卡新建的文件夹下
	public final static String WORKERPATH = "/worker"; // 工作目录文件夹
	public final static String LOGPATH = "/log";// 日志文件夹
	public static String ROOTPATH;// 项目中需要用到的解压包，置于data/data 下

	public static String LOG_TERMINAL_FILE_NAME = "terminal.log";// 终端日志文件名

	public static String LOG_TERMINAL_INFO_NAME = "terminalInfo.txt"; // 终端信息文件名

	public File ApkDownloadPath; // apk下载地址
	public File BizDownLoadPath; // biz下载地址
	public File ImgDownLoadPath; // image下载地址
	public File ResDownLoadPath; // res下载地址

	public File BizUserPath; // 解压后的biz路径 rootpath下
	public File ImgUserPath; // 解压后的image路径
	public File ResUserPath; // 解压后的res路径

	public static String DOWNLOAD_PATH; // download路径
	public static String APK_DOWNLOAD_PATH; // apk下载路径
	public static String BIZ_DOWNLOAD_PATH; // biz下载路径(图片与biz合在了一起)
	public static String IMG_DOWNLOAD_PATH; // 图片下载路径
	public static String RES_DOWNLOAD_PATH; // H5资源下载路径

	public static String USER_PATH; // 工作路径
	public static String BIZ_USER_PATH; // biz工作路径
	public static String IMG_USER_PATH; // 图片资源工作路径
	public static String RES_USER_PATH; // H5资源工作路径
	public static String PRINT_USER_PATH; // 打印资源文件资源
	public static String APK_USER_PATH; // APK资源文件资源

	public static String LOG_PATH;// 导出日志路径

	public File XLPath;
	public Context mContext;
	private static FileUtil mInstance;

	public FileUtil(Context context) {
		mContext = context;
	}

	/**
	 * 创建文件工具类示例
	 * 
	 * @param context
	 *            上下文
	 * @return
	 */
	public static synchronized FileUtil createInstance(Context context) {
		if (mInstance == null) {
			mInstance = new FileUtil(context);
			mInstance.initPath();
		}
		return mInstance;
	}

	/**
	 * 获取文件工具类实例
	 * 
	 * @return
	 */
	public static synchronized FileUtil getInstance() {
		if (mInstance == null)
			throw new IllegalStateException(
					"FileUtil must be create by call createInstance(Context context)");
		return mInstance;
	}

	/**
	 * 初始化本地缓存路径
	 */
	public void initPath() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			SDPATH = Environment.getExternalStorageDirectory() + "/";
			ROOTPATH = getFileRoot();

			DOWNLOAD_PATH = ROOTPATH + DOWNLOADPATH;
			USER_PATH = ROOTPATH + WORKERPATH;
			LOG_PATH = ROOTPATH + LOGPATH;

			APK_DOWNLOAD_PATH = DOWNLOAD_PATH + "/apk";
			BIZ_DOWNLOAD_PATH = DOWNLOAD_PATH + "/bizlist";
			IMG_DOWNLOAD_PATH = DOWNLOAD_PATH + "/img";
			RES_DOWNLOAD_PATH = DOWNLOAD_PATH + "/res";

			BIZ_USER_PATH = USER_PATH + "/bizlist";
			IMG_USER_PATH = USER_PATH + "/img";
			RES_USER_PATH = USER_PATH + "/res";
			PRINT_USER_PATH = USER_PATH + "/print";
			APK_USER_PATH = USER_PATH + "/apk";

			XLPath = new File(APK_DOWNLOAD_PATH);
			if (!XLPath.exists()) {
				XLPath.mkdirs();
			}
			XLPath = new File(BIZ_DOWNLOAD_PATH);
			if (!XLPath.exists()) {
				XLPath.mkdirs();
			}
			XLPath = new File(IMG_DOWNLOAD_PATH);
			if (!XLPath.exists()) {
				XLPath.mkdirs();
			}
			XLPath = new File(RES_DOWNLOAD_PATH);
			if (!XLPath.exists()) {
				XLPath.mkdirs();
			}

			XLPath = new File(BIZ_USER_PATH);
			if (!XLPath.exists()) {
				XLPath.mkdirs();
			}
			XLPath = new File(IMG_USER_PATH);
			if (!XLPath.exists()) {
				XLPath.mkdirs();
			}
			XLPath = new File(RES_USER_PATH);
			if (!XLPath.exists()) {
				XLPath.mkdirs();
			}
			XLPath = new File(PRINT_USER_PATH);
			if (!XLPath.exists()) {
				XLPath.mkdirs();
			}
			XLPath = new File(APK_USER_PATH);
			if (!XLPath.exists()) {
				XLPath.mkdirs();
			}
		}

	}

	// 文件存储根目录
	public String getFileRoot() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
			// File external = mContext.getExternalFilesDir(null);
			File external = mContext.getFilesDir();
			if (external != null) {
				return external.getAbsolutePath();
			}
		}
		return mContext.getFilesDir().getAbsolutePath();
	}

	public static String sdCardPath(String fileName) {
		String IMAGES_PATH = Environment.getExternalStorageDirectory() + "/"
				+ fileName + "/";

		File imgFile = new File(IMAGES_PATH);
		if (!imgFile.exists()) {
			imgFile.mkdir();
		}
		return imgFile.toString();
	}

	/**
	 * [将文件保存到SDcard方法]
	 * 
	 * @param fileName
	 * @param inStream
	 * @throws IOException
	 */
	public boolean saveFile2SDCard(String fileName, byte[] dataBytes)
			throws IOException {
		boolean flag = false;
		FileOutputStream fs = null;
		try {
			if (!TextUtils.isEmpty(fileName)) {
				File file = newFileWithPath(fileName.toString());
				if (file.exists()) {
					file.delete();
					Log.w(TAG, "httpFrame  threadName:"
							+ Thread.currentThread().getName()
							+ " 文件已存在 则先删除: " + fileName.toString());
				}
				fs = new FileOutputStream(file);
				fs.write(dataBytes, 0, dataBytes.length);
				fs.flush();
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fs != null)
				fs.close();
		}
		return flag;
	}

	/**
	 * 创建一个文件，如果其所在目录不存在时，他的目录也会被跟着创建
	 * 
	 */
	public File newFileWithPath(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return null;
		}

		int index = filePath.lastIndexOf(File.separator);

		String path = "";
		if (index != -1) {
			path = filePath.substring(0, index);
			if (!TextUtils.isEmpty(path)) {
				File file = new File(path.toString());
				// 如果文件夹不存在
				if (!file.exists() && !file.isDirectory()) {
					boolean flag = file.mkdirs();
					if (flag) {
						Log.i(TAG, "httpFrame  threadName:"
								+ Thread.currentThread().getName()
								+ " 创建文件夹成功：" + file.getPath());
					} else {
						Log.e(TAG, "httpFrame  threadName:"
								+ Thread.currentThread().getName()
								+ " 创建文件夹失败：" + file.getPath());
					}
				}
			}
		}
		return new File(filePath);
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param strPath
	 * @return
	 */
	public boolean isExists(String strPath) {
		if (strPath == null) {
			return false;
		}
		final File strFile = new File(strPath);
		return strFile.exists();
	}

	/**
	 * 将assest 中的文件复制到sd卡中
	 * 
	 * @param strOutFileName
	 * @throws IOException
	 */
	public void copyBigDataToSD(Context context, String inputFileName,
			String strOutFileName) throws IOException {

		InputStream myInput;
		OutputStream myOutput = new FileOutputStream(strOutFileName);
		myInput = context.getAssets().open(inputFileName);
		byte[] buffer = new byte[1024];
		int length = myInput.read(buffer);
		while (length > 0) {
			myOutput.write(buffer, 0, length);
			length = myInput.read(buffer);
		}

		myOutput.flush();
		myInput.close();
		myOutput.close();
	}

	/**
	 * 复制目录文件
	 * 
	 * @param fromFile
	 * @param toFile
	 * @return
	 */
	public boolean copy(String fromFile, String toFile) {
		// 要复制的文件目录
		File[] currentFiles;
		File root = new File(fromFile);
		// 如同判断SD卡是否存在或者文件是否存在
		// 如果不存在则 return出去
		if (!root.exists()) {
			return false;
		}
		// 如果存在则获取当前目录下的全部文件 填充数组
		currentFiles = root.listFiles();

		// 目标目录
		File targetDir = new File(toFile);
		// 创建目录
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
		// 遍历要复制该目录下的全部文件
		for (int i = 0; i < currentFiles.length; i++) {
			if (currentFiles[i].isDirectory()) {
				// 如果当前项为子目录 进行递归
				copy(currentFiles[i].getPath() + "/",
						toFile + currentFiles[i].getName() + "/");
			} else {
				// 如果当前项为文件则进行文件拷贝
				CopySdcardFile(currentFiles[i].getPath(), toFile
						+ currentFiles[i].getName());
			}
		}
		return true;
	}

	/**
	 * 文件拷贝 要复制的目录下的所有非子目录(文件夹)文件拷贝
	 * 
	 * @param fromFile
	 * @param toFile
	 * @return
	 */
	public static boolean CopySdcardFile(String fromFile, String toFile) {

		try {
			InputStream fosfrom = new FileInputStream(fromFile);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			File imagefile = new File(toFile);
			if (!imagefile.exists()) {
				imagefile.createNewFile();
			}
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = fosfrom.read(buffer)) > 0) {
				baos.write(buffer, 0, count);
			}
			fosfrom.close();
			baos.flush();
			baos.close();
			return true;

		} catch (Exception ex) {
			PosLogger.e(TAG, ex.getMessage(), ex);
			return false;
		}
	}

	/**
	 * copy图片到SD卡中
	 * @param fromFile 原图片路劲
	 * @param toFile  新图片路劲
	 * @throws Exception 
	 */
	public static void copyPicToSd (String fromFile, String toFile) throws Exception  {
		File imagefile = new File(toFile);
		
		try {
			if (!imagefile.exists()) {
				imagefile.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(imagefile);
			Bitmap colorImage = BitmapFactory.decodeFile(fromFile, null);
			colorImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			PosLogger.e(TAG,"fromFile:"+fromFile+",toFile:"+toFile+","+e.getMessage(),e);
		    throw e;
		}

	}

	/**
	 * 读取文件夹下的文件流
	 * 
	 * @param fileDir
	 * @param fileName
	 * @return InputStream
	 */
	public InputStream readInputStream(File fileDir, String fileName) {
		InputStream in = null;
		File file = new File(fileDir, fileName);
		try {
			@SuppressWarnings("resource")
			FileInputStream inputStream = new FileInputStream(file);
			byte[] b = new byte[inputStream.available()];
			inputStream.read(b);
			in = new ByteArrayInputStream(b);

		} catch (Exception e) {
			in = null;
		}
		return in;

	}

	/**
	 * 获取文件夹的大小 Context.getExternalFilesDir() -->
	 * SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
	 * Context.getExternalCacheDir() -->
	 * SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String getCacheSize(File file) throws Exception {
		return getFormatSize(getFolderSize(file));
	}

	/**
	 * 获取文件大小
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	/**
	 * 格式化单位
	 * 
	 * @param size
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			return size + "Byte";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
				+ "TB";
	}

	/**
	 * 删除文件夹
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	/**
	 * 写文件
	 * 
	 * @param logFile
	 * @return 是否成功
	 */
	public static boolean writeStrFile(String sb, String logFile) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(logFile);
			fos.write(sb.getBytes());
			fos.flush();
			fos.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			PosLogger.e(TAG, e.getMessage(), e);
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			PosLogger.e(TAG, e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 修改文件读取权限
	 * 
	 * @param bizcodestr
	 */
	public static void changChmodDir1(String bizcodestr) {
		File destDir = null;
		if ("PRINT-ORG-LOG".equals(bizcodestr)) {
			destDir = new File(RES_USER_PATH
					+ "/PRINT-ORG-LOG/print_org_logo.bmp");
		} else if ("PRINT-MCHT-ADS-LOG".equals(bizcodestr)) {
			destDir = new File(RES_USER_PATH
					+ "/PRINT-MCHT-ADS-LOG/print_mctht_ads_log.bmp");
		}

		if (!destDir.exists()) {
			destDir.getParentFile().mkdirs();
		}
		Process p;
		int status = -1;
		try {
			p = Runtime.getRuntime().exec("chmod 777 " + destDir);
			p = Runtime.getRuntime().exec(
					"chmod 777 " + destDir.getParentFile());
			status = p.waitFor();
		} catch (IOException e1) {
			e1.printStackTrace();
			PosLogger.e(TAG, e1.getMessage(), e1);
		} catch (InterruptedException e) {
			e.printStackTrace();
			PosLogger.e(TAG, e.getMessage(), e);
		}
		if (status == 0) {
			PosLogger.i(TAG, "权限修改成功");
		} else {
			PosLogger.i(TAG, "权限修改失败");
		}
	}

}