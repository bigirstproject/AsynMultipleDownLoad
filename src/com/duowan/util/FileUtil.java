package com.duowan.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import org.apache.http.util.ByteArrayBuffer;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

public class FileUtil {
	public static final String ROOT_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + File.separator;

	private static final String DOWNLOAD_ROOT_PATH = "kugouRing";

	public static final String DOWNLOAD_ROOT_DIR_PATH = ROOT_PATH
			+ File.separator + DOWNLOAD_ROOT_PATH;

	public static final String DOWNLOAD_CACHE_PATH = DOWNLOAD_ROOT_DIR_PATH
			+ File.separator + ".cache";

	public static final String MAKE_DIR_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ DOWNLOAD_ROOT_PATH + File.separator + "Make" + File.separator;

	public static final String RECORD_DIR_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ DOWNLOAD_ROOT_PATH + File.separator + "record" + File.separator;

	public static final String RECORD_DIR_PATH_CACHE = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ DOWNLOAD_ROOT_PATH
			+ File.separator
			+ "record"
			+ File.separator
			+ ".cache" + File.separator;

	public static final String PACK_RANGTONE_DIR_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ DOWNLOAD_ROOT_PATH + File.separator + "packimg" + File.separator;

	//

	private FileUtil() {

	}

	/**
	 * 文件是否存在
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean fileIsExist(String filePath) {
		if (filePath == null || filePath.length() < 1) {
			return false;
		}

		File f = new File(filePath);
		if (!f.exists()) {
			return false;
		}
		return true;
	}

	/**
	 * 创建文件目录
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean createDirectory(String filePath) {
		if (null == filePath) {
			return false;
		}

		File file = new File(filePath);

		if (file.exists()) {
			return true;
		}

		return file.mkdirs();
	}

	/**
	 * 删除目录 及其 子目录
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean deleteDirectory(String filePath) {
		if (null == filePath) {
			LogCat.d("param invalid, filePath: " + filePath);
			return false;
		}

		File file = new File(filePath);

		if (file == null || !file.exists()) {
			return false;
		}

		if (file.isDirectory()) {
			File[] list = file.listFiles();

			for (int i = 0; i < list.length; i++) {
				LogCat.d("delete filePath: " + list[i].getAbsolutePath());

				if (list[i].isDirectory()) {
					deleteDirectory(list[i].getAbsolutePath());
				} else {
					list[i].delete();
				}
			}
		}

		LogCat.d("delete filePath: " + file.getAbsolutePath());

		file.delete();
		return true;
	}

	/**
	 * 获取文件大小
	 * 
	 * @param filePath
	 * @return
	 */
	public static long getFileSize(String filePath) {
		if (null == filePath) {
			LogCat.d("Invalid param. filePath: " + filePath);
			return 0;
		}
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return 0;
		}
		return file.length();
	}

	/**
	 * 获取文件夹的大小
	 * 
	 * @param filePath
	 * @return
	 */
	public static double getFilesSize(String filePath) {
		double size = 0l;
		File[] listFile = listFiles(filePath);
		for (int i = 0; i < listFile.length; i++) {
			if (listFile[i].exists() && listFile[i].isDirectory()) {
				size += getFilesSize(listFile[i].getAbsolutePath());
			} else {
				size += getFilesSize(listFile[i].getAbsolutePath());
			}
		}
		return size;
	}

	/**
	 * 转化文件大小
	 * 
	 * @param size
	 * @return
	 */
	public String getChangeSize(double size) {
		StringBuffer sb = new StringBuffer();
		int nums = 0;
		if (size > 1024 * 1024 * 1024) {
			nums = (int) (size / 1024 / 1024 / 1024);
			sb.append(nums + ".");
			if (size / 1024 / 1024 % 1024 > 1024 * 1024) {
				nums = (int) (size / 1024 / 1024 % 1024 / 1024 / 1024);
				sb.append(nums + "G");
			}
		} else if (size > 1024 * 1024) {
			nums = (int) (size / 1024 / 1024);
			sb.append(nums + ".");
			if (size / 1024 % 1024 > 1024) {
				nums = (int) (size / 1024 % 1024 / 1024);
				sb.append(nums + "M");
			}
		} else if (size > 1024) {
			nums = (int) (size / 1024);
			sb.append(nums + "K");
		} else {
			sb.append(nums + "B");
		}
		return sb.toString();
	}

	/**
	 * 获取文件最后修改时间
	 * 
	 * @param filePath
	 * @return
	 */
	public static long getFileModifyTime(String filePath) {
		if (null == filePath) {
			LogCat.d("Invalid param. filePath: " + filePath);
			return 0;
		}

		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return 0;
		}

		return file.lastModified();
	}

	/**
	 * 设置文件最后修改时间
	 * 
	 * @param filePath
	 * @param modifyTime
	 * @return
	 */
	public static boolean setFileModifyTime(String filePath, long modifyTime) {
		if (null == filePath) {
			LogCat.d("Invalid param. filePath: " + filePath);
			return false;
		}

		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return false;
		}

		return file.setLastModified(modifyTime);
	}

	/**
	 * 将byte[]写入文件
	 * 
	 * @attention 当文件存在将被替换 当其所在目录不存在，将尝试创建
	 * @param filePath
	 *            格式如： /sdcard/abc/a.obj
	 * @param content
	 *            写入内容byte[]
	 * @return
	 */
	public static boolean writeFile(String filePath, byte[] content) {
		if (null == filePath || null == content) {
			LogCat.d("Invalid param. filePath: " + filePath + ", content: "
					+ content);
			return false;
		}

		FileOutputStream fos = null;
		try {
			String pth = filePath.substring(0, filePath.lastIndexOf("/"));
			File pf = null;
			pf = new File(pth);
			if (pf.exists() && !pf.isDirectory()) {
				pf.delete();
			}
			pf = new File(filePath);
			if (pf.exists()) {
				if (pf.isDirectory())
					deleteDirectory(filePath);
				else
					pf.delete();
			}

			pf = new File(pth + File.separator);
			if (!pf.exists()) {
				if (!pf.mkdirs()) {
					LogCat.d("Can't make dirs, path=" + pth);
				}
			}

			fos = new FileOutputStream(filePath);
			fos.write(content);
			fos.flush();
			fos.close();
			fos = null;
			pf.setLastModified(System.currentTimeMillis());

			return true;

		} catch (Exception ex) {
			LogCat.d("Exception, ex: " + ex.toString());
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		}
		return false;
	}

	/**
	 * 读取文件
	 * 
	 * @param filePath
	 * @return 输入流
	 */
	public static InputStream readFile(String filePath) {
		if (null == filePath) {
			LogCat.d("Invalid param. filePath: " + filePath);
			return null;
		}

		InputStream is = null;

		try {
			if (fileIsExist(filePath)) {
				File f = new File(filePath);
				is = new FileInputStream(f);
			} else {
				return null;
			}
		} catch (Exception ex) {
			LogCat.d("Exception, ex: " + ex.toString());
			return null;
		}
		return is;
	}

	/**
	 * 读取输入流 转化为 byte[]
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static byte[] readIn(InputStream is) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		byte[] buf = new byte[1024];
		int c = is.read(buf);
		while (-1 != c) {
			baos.write(buf, 0, c);
			c = is.read(buf);
		}
		baos.flush();
		baos.close();
		return baos.toByteArray();
	}

	/**
	 * 创建文件的模式，已经存在的文件要覆盖
	 */
	public final static int MODE_COVER = 1;

	/**
	 * 创建文件的模式，文件已经存在则不做其它事
	 */
	public final static int MODE_UNCOVER = 0;

	/**
	 * 获取文件的输入流
	 * 
	 * @param path
	 * @return
	 */
	public static FileInputStream getFileInputStream(String path) {
		FileInputStream fis = null;
		try {
			File file = new File(path);
			if (file.exists()) {
				fis = new FileInputStream(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fis;
	}

	/**
	 * 获取文件的输出流
	 * 
	 * @param path
	 * @return
	 */
	public static OutputStream getFileOutputStream(String path) {
		FileOutputStream fos = null;
		try {
			File file = new File(path);
			if (file.exists()) {
				fos = new FileOutputStream(file);
			}
		} catch (Exception e) {
			return null;
		}
		return fos;
	}

	/**
	 * 获取文件的数据
	 * 
	 * @param path
	 * @return
	 */
	public static byte[] getFileData(String path) {
		byte[] data = null;// 返回的数据
		try {
			File file = new File(path);
			if (file.exists()) {
				data = new byte[(int) file.length()];
				FileInputStream fis = new FileInputStream(file);
				fis.read(data);
				fis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 写入新文件
	 */
	public static void writeData(String path, byte[] data) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				FileOutputStream fos = new FileOutputStream(file, false);
				fos.write(data);
				fos.flush();
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重写文件的数据
	 * 
	 * @param path
	 * @param data
	 */
	public static void rewriteData(String path, byte[] data) {
		try {
			File file = new File(path);
			if (file.exists()) {
				FileOutputStream fos = new FileOutputStream(file, false);
				fos.write(data);
				fos.flush();
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重写文件的数据
	 * 
	 * @param path
	 * @param is
	 */
	public static void rewriteData(String path, InputStream is) {
		try {
			File file = new File(path);
			if (file.exists()) {
				FileOutputStream fos = new FileOutputStream(file, false);
				byte[] data = new byte[1024];
				int receive = 0;
				while ((receive = is.read(data)) != -1) {
					fos.write(data, 0, receive);
					fos.flush();
				}
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向文件的末尾添加数据
	 * 
	 * @param path
	 * @param data
	 */
	public static boolean appendData(String path, byte[] data) {
		try {
			File file = new File(path);
			if (file.exists()) {
				FileOutputStream fos = new FileOutputStream(file, true);
				fos.write(data);
				fos.flush();
				fos.close();
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 向文件末尾添加数据
	 * 
	 * @param path
	 * @param is
	 */
	public static void appendData(String path, InputStream is) {
		try {
			File file = new File(path);
			if (file.exists()) {
				FileOutputStream fos = new FileOutputStream(file, true);
				byte[] data = new byte[1024];
				int receive = 0;
				while ((receive = is.read(data)) != -1) {
					fos.write(data, 0, receive);
					fos.flush();
				}
				fos.close();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 删除文件或文件夹(包括目录下的文件)
	 * 
	 * @param path
	 */
	public static void deleteFile(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return;
		}
		try {
			File f = new File(filePath);
			if (f.exists() && f.isDirectory()) {
				File[] delFiles = f.listFiles();
				if (delFiles != null) {
					for (int i = 0; i < delFiles.length; i++) {
						deleteFile(delFiles[i].getAbsolutePath());
					}
				}
			}
			f.delete();
		} catch (Exception e) {

		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 * @param deleteParent
	 *            是否删除父目录
	 */
	public static void deleteFile(String filePath, boolean deleteParent) {
		if (filePath == null) {
			return;
		}
		try {
			File f = new File(filePath);
			if (f.exists() && f.isDirectory()) {
				File[] delFiles = f.listFiles();
				if (delFiles != null) {
					for (int i = 0; i < delFiles.length; i++) {
						deleteFile(delFiles[i].getAbsolutePath(), deleteParent);
					}
				}
			}
			if (deleteParent) {
				f.delete();
			} else if (f.isFile()) {
				f.delete();
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 创建一个空的文件(创建文件的模式，已经存在的是否要覆盖)
	 * 
	 * @param path
	 * @param mode
	 */
	public static boolean createFile(String path, int mode) {
		if (TextUtils.isEmpty(path)) {
			return false;
		}
		try {
			File file = new File(path);
			if (file.exists()) {
				if (mode == FileUtil.MODE_COVER) {
					file.delete();
					file.createNewFile();
				}
			} else {
				// 如果路径不存在，先创建路径
				File mFile = file.getParentFile();
				if (!mFile.exists()) {
					mFile.mkdirs();
				}
				file.createNewFile();
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 创建一个空的文件夹(创建文件夹的模式，已经存在的是否要覆盖)
	 * 
	 * @param path
	 * @param mode
	 */
	public static void createFolder(String path, int mode) {
		try {
			File file = new File(path);
			if (file.exists()) {
				if (mode == FileUtil.MODE_COVER) {
					file.delete();
					file.mkdirs();
				}
			} else {
				file.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取文件大小
	 * 
	 * @param path
	 * @return
	 */
	public static long getSize(String path) {
		if (TextUtils.isEmpty(path)) {
			return 0;
		}
		long size = 0;
		try {
			File file = new File(path);
			if (file.exists()) {
				size = file.length();
			}
		} catch (Exception e) {
			return 0;
		}
		return size;
	}

	/**
	 * 判断文件或文件夹是否存在
	 * 
	 * @param path
	 * @return true 文件存在
	 */
	public static boolean isExist(String path) {
		if (TextUtils.isEmpty(path)) {
			return false;
		}
		boolean exist = false;
		try {
			File file = new File(path);
			exist = file.exists();
		} catch (Exception e) {
			return false;
		}
		return exist;
	}

	/**
	 * 重命名文件/文件夹
	 * 
	 * @param path
	 * @param newName
	 */
	public static boolean rename(final String path, final String newName) {
		boolean result = false;
		if (TextUtils.isEmpty(path) || TextUtils.isEmpty(newName)) {
			return result;
		}
		try {
			File file = new File(path);
			if (file.exists()) {
				result = file.renameTo(new File(newName));
			}
		} catch (Exception e) {
		}

		return result;
	}

	/**
	 * 列出目录文件
	 * 
	 * @return
	 */
	public static File[] listFiles(String filePath) {
		File file = new File(filePath);
		if (file.exists() && file.isDirectory()) {
			return file.listFiles();
		}
		return null;
	}

	/**
	 * @param path
	 *            hash.ext.kgtmp
	 * @return
	 */
	public static String getAudioMimeType(String path) {
		boolean isM4A = path.toLowerCase().endsWith(".m4a");
		return isM4A ? "audio/mp4" : "audio/mpeg";
	}

	/**
	 * 是否是m4a文件
	 * 
	 * @param m4a
	 *            m4a文件路径
	 * @return
	 */
	public static boolean isM4A(final String m4a) {
		if (TextUtils.isEmpty(m4a)) {
			return false;
		}
		try {
			FileInputStream stream = new FileInputStream(new File(m4a));
			byte[] buffer = new byte[8];
			if (stream.read(buffer) == 8) {
				stream.close();
				return (buffer[4] == 'f' && buffer[5] == 't'
						&& buffer[6] == 'y' && buffer[7] == 'p');
			} else {
				stream.close();
				return false;
			}
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * 哈希值写到m4a
	 * 
	 * @param m4a
	 *            m4a文件路径
	 * @param hash
	 *            mp3哈希值 kgmp3hash
	 */
	public static void writeMp3HashToM4a(final String m4a, final String hash) {
		if (TextUtils.isEmpty(m4a) || TextUtils.isEmpty(hash)) {
			return;
		}
		try {
			File m4afile = new File(m4a);
			RandomAccessFile accessFile = new RandomAccessFile(m4afile, "rw");
			long m4aLength = m4afile.length();
			byte[] tagbyte = TAG_KGMP3HASH.getBytes();
			byte[] hashbyte = hash.getBytes();
			ByteArrayBuffer buffer = new ByteArrayBuffer(TAG_KGMP3HASH_LENGTH);
			buffer.append(tagbyte, 0, tagbyte.length);
			buffer.append(hashbyte, 0, hashbyte.length);
			accessFile.skipBytes((int) m4aLength);
			accessFile.write(buffer.toByteArray());
			accessFile.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	private static final String TAG_KGMP3HASH = "kgmp3hash";

	private static final int TAG_KGMP3HASH_LENGTH = TAG_KGMP3HASH.length() + 32;

	/**
	 * 从m4a读取mp3哈希值
	 * 
	 * @param m4a
	 *            m4a文件路径
	 * @return
	 */
	public static String readMp3HashFromM4a(final String m4a) {
		if (TextUtils.isEmpty(m4a)) {
			return null;
		}
		File m4afile = new File(m4a);
		RandomAccessFile accessFile = null;
		try {
			accessFile = new RandomAccessFile(m4afile, "r");
			accessFile
					.skipBytes((int) (m4afile.length() - TAG_KGMP3HASH_LENGTH));
			byte[] b = new byte[TAG_KGMP3HASH_LENGTH];
			if (accessFile.read(b) == TAG_KGMP3HASH_LENGTH) {
				String taghash = new String(b);
				if (!TextUtils.isEmpty(taghash)
						&& taghash.startsWith(TAG_KGMP3HASH)) {
					return taghash.substring(TAG_KGMP3HASH.length());
				}
			}
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			if (accessFile != null) {
				try {
					accessFile.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	/**
	 * 移动文件
	 * 
	 * @param oldFilePath
	 *            旧路径
	 * @param newFilePath
	 *            新路径
	 * @return
	 */
	public static boolean moveFile(String oldFilePath, String newFilePath) {
		if (TextUtils.isEmpty(oldFilePath) || TextUtils.isEmpty(newFilePath)) {
			return false;
		}
		File oldFile = new File(oldFilePath);
		if (oldFile.isDirectory() || !oldFile.exists()) {
			return false;
		}
		try {
			File newFile = new File(newFilePath);
			if (!newFile.exists()) {
				newFile.createNewFile();
			}
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(oldFile));
			FileOutputStream fos = new FileOutputStream(newFile);
			byte[] buf = new byte[1024];
			int read;
			while ((read = bis.read(buf)) != -1) {
				fos.write(buf, 0, read);
			}
			fos.flush();
			fos.close();
			bis.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static byte[] InputStreamToByte(InputStream iStrm)
			throws IOException {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = iStrm.read()) != -1) {
			bytestream.write(ch);
		}
		byte imgdata[] = bytestream.toByteArray();
		bytestream.close();
		return imgdata;
	}

	/**
	 * 是否是下载出错文件（下到错误页面的数据）
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 */
	public static boolean isErrorFile(final String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return false;
		}
		try {
			FileInputStream stream = new FileInputStream(new File(filePath));
			byte[] buffer = new byte[16];
			if (stream.read(buffer) == 16) {
				stream.close();
				return ((buffer[0] & 0xFF) == 0xFF
						&& (buffer[1] & 0xFF) == 0xD8
						&& (buffer[2] & 0xFF) == 0xFF
						&& (buffer[3] & 0xFF) == 0xE0
						&& (buffer[4] & 0xFF) == 0x00
						&& (buffer[5] & 0xFF) == 0x10
						&& (buffer[6] & 0xFF) == 0x4A
						&& (buffer[7] & 0xFF) == 0x46
						&& (buffer[8] & 0xFF) == 0x49
						&& (buffer[9] & 0xFF) == 0x46
						&& (buffer[10] & 0xFF) == 0x00
						&& (buffer[11] & 0xFF) == 0x01
						&& (buffer[12] & 0xFF) == 0x02
						&& (buffer[13] & 0xFF) == 0x01
						&& (buffer[14] & 0xFF) == 0x00 && (buffer[15] & 0xFF) == 0x48);
			} else {
				stream.close();
				return false;
			}
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	public static long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	public static long getmem_UNUSED(Context mContext) {
		long MEM_UNUSED;
		// 得到ActivityManager
		ActivityManager am = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 创建ActivityManager.MemoryInfo对象

		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(mi);

		// 取得剩余的内存空间

		MEM_UNUSED = mi.availMem / 1024;
		return MEM_UNUSED;
	}

}
