package com.huiyi.nypos.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import android.util.Base64;
import android.util.Log;

public class ZipUtil {
	private static String TAG = ZipUtil.class.getSimpleName();

	private ZipUtil() {
	}

	/**
	 * 解压文件 DeCompress the ZIP to the path
	 * 
	 * @param zipFileString
	 *            name of ZIP
	 * @param outPathString
	 *            path to be unZIP
	 * @throws Exception
	 */
	public static boolean unZipFolder(String zipFilePath, String outPath)
			throws Exception {
		ZipInputStream inZip = null;
		ZipEntry zipEntry;
		boolean ret = true;
		Log.i("unZipFolder", zipFilePath);
		String szName = "";
		Log.d(TAG, "start unZipFolder");
		// /data/data/com.huiyi.nypos.luncher/files/download/res/BIZ-NATIONAL-CARD.zip
		try {
			FileInputStream inputStream = new FileInputStream(zipFilePath);
			inZip = new ZipInputStream(inputStream);
			while ((zipEntry = inZip.getNextEntry()) != null) {

				szName = zipEntry.getName();
				Log.d(TAG, szName);
				if (zipEntry.isDirectory()) {
					// get the folder name of the widget
					szName = szName.substring(0, szName.length() - 1);
					File folder = new File(outPath + File.separator + szName);
					folder.mkdirs();
				} else {

					File file = new File(outPath + File.separator + szName);
					file.createNewFile();
					// get the output stream of the file
					FileOutputStream out = null;
					try {
						out = new FileOutputStream(file);
						int len;
						byte[] buffer = new byte[1024];
						// read (len) bytes into buffer
						while ((len = inZip.read(buffer)) != -1) {
							// write (len) byte from buffer at the position 0
							out.write(buffer, 0, len);
							out.flush();
						}
					} catch (Exception e) {
						Log.e(TAG, "unZipFolder fail ", e);
						ret = false;
					} finally {
						out.close();
					}
				}

				if (!ret) // 出现异常后终止执行后续解压
					break;
			}

		} catch (Exception e) {
			Log.e(TAG, "unZipFolder fail ", e);
			ret = false;
		} finally {
			if (inZip != null)
				try {
					inZip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		Log.d(TAG, "end unZipFolder");

		if (!ret) // 处理失败时，清理数据
			deleteFile(outPath);

		return ret;
	}

	/**
	 * 压缩一个文件夹 Compress file and folder
	 * 
	 * @param srcFileString
	 *            file or folder to be Compress
	 * @param zipFileString
	 *            the path name of result ZIP
	 * @throws Exception
	 */
	public static boolean zipFolder(String srcPath, String zipFilePath) {

		boolean ret = true;// 默认成功

		Log.d(TAG, "start  zipFolder");

		// create ZIP
		ZipOutputStream outZip = null;
		try {
			FileOutputStream outputStream = new FileOutputStream(zipFilePath);
			outZip = new ZipOutputStream(outputStream);
			// create the file
			File file = new File(srcPath);
			// compress
			ret = zipFiles(file.getParent() + File.separator, file.getName(),
					outZip);
			// finish and close
			outZip.finish();
		} catch (Exception e) {
			Log.e(TAG, "zipFolder fail", e);
			ret = false;
		} finally {
			try {
				outZip.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (!ret) // 处理失败时，清理数据
			deleteFile(zipFilePath);
		Log.d(TAG, "end  zipFolder");

		return ret;
	}

	/**
	 * compress files 压缩文件
	 * 
	 * @param folderString
	 * @param fileString
	 * @param zipOutputSteam
	 * @throws Exception
	 */
	private static boolean zipFiles(String folderString, String fileString,
			ZipOutputStream zipOutputSteam) {

		boolean ret = true; // 默认成功

		File file = new File(folderString + fileString);
		if (file.isFile()) {
			ZipEntry zipEntry = new ZipEntry(fileString);
			FileInputStream inputStream = null;
			try {
				inputStream = new FileInputStream(file);
				zipOutputSteam.putNextEntry(zipEntry);
				int len;
				byte[] buffer = new byte[4096];
				while ((len = inputStream.read(buffer)) != -1) {
					zipOutputSteam.write(buffer, 0, len);
				}
			} catch (Exception e) {
				Log.e(TAG, "zipFiles fail", e);
				ret = false;
			} finally {
				if (inputStream != null)
					try {
						inputStream.close();
						zipOutputSteam.closeEntry();
					} catch (Exception e) {
						e.printStackTrace();
					}
			}

		} else {
			// folder
			String fileList[] = file.list();
			// no child file and compress
			if (fileList.length <= 0) {
				ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
				try {
					zipOutputSteam.putNextEntry(zipEntry);
				} catch (Exception e) {
					Log.e(TAG, "zipFiles fail", e);
					ret = false;
				} finally {
					try {
						zipOutputSteam.closeEntry();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			if (!ret) // 处理失败时终止
				return ret;
			// child files and recursion
			for (int i = 0; i < fileList.length; i++) {
				ret = zipFiles(folderString, fileString
						+ java.io.File.separator + fileList[i], zipOutputSteam);
				if (!ret) // 处理失败时终止
					return ret;
			}// end of for
		}

		return ret;
	}

	/**
	 * return the InputStream of file in the ZIP
	 * 
	 * @param zipFileString
	 *            name of ZIP
	 * @param fileString
	 *            name of file in the ZIP
	 * @return InputStream
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static InputStream extractFileStreamFromZip(String zipFilePath,
			String fileName) throws Exception {
		ZipFile zipFile = new ZipFile(zipFilePath);
		ZipEntry zipEntry = zipFile.getEntry(fileName);
		return zipFile.getInputStream(zipEntry);

	}

	/**
	 * return files list(file and folder) in the ZIP
	 * 
	 * @param zipFileString
	 *            ZIP name
	 * @param bContainFolder
	 *            contain folder or not
	 * @param bContainFile
	 *            contain file or not
	 * @return
	 * @throws Exception
	 */
	public static List<File> getFileList(String zipFilePath,
			boolean containFolder, boolean containFile) {
		List<File> fileList = new ArrayList<File>();
		ZipInputStream inZip = null;

		try {
			inZip = new ZipInputStream(new FileInputStream(zipFilePath));
			ZipEntry zipEntry;
			String szName = "";
			while ((zipEntry = inZip.getNextEntry()) != null) {
				szName = zipEntry.getName();
				if (zipEntry.isDirectory()) {
					// get the folder name of the widget
					szName = szName.substring(0, szName.length() - 1);
					File folder = new File(szName);
					if (containFolder) {
						fileList.add(folder);
					}

				} else {
					File file = new File(szName);
					if (containFile) {
						fileList.add(file);
					}
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "getFileList fail", e);
			fileList = null;
		} finally {
			try {
				inZip.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileList;
	}

	/**
	 * 用于解压或者压缩失败的时候，删除掉新建的文件
	 * 
	 * @param filePath
	 */
	private static void deleteFile(String filePath) {
		try {
			File file = new File(filePath);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 字符串解压缩
	 * 
	 * @param zipStr
	 * @return
	 */
	public static String unZipStr(String zipStr) {
		// TODO Auto-generated method stub
		if (zipStr == null) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InflaterOutputStream in = null;
		byte[] compressed = null;
		String decompressed = null;
		try {
			compressed = Base64.decode(zipStr, Base64.NO_WRAP);
			out = new ByteArrayOutputStream();
			in = new InflaterOutputStream(out);
			try {
				in.write(compressed);
				in.close();
				decompressed = new String(out.toByteArray(), "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return decompressed;
	}
	
	// 压缩 字符串 
    public static String zipStr(String str) throws IOException {   
      if (str == null || str.length() == 0) {   
        return str;   
      }   
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      
      GZIPOutputStream gzip = new GZIPOutputStream(out);   
      gzip.write(str.getBytes());
      gzip.close(); 
     
      String zipStr = out.toString("UTF-8");
      byte[] compressed = Base64.decode(zipStr, Base64.NO_WRAP);
      return   new String(compressed);
    }  
}
