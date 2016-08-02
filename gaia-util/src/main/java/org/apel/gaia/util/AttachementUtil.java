package org.apel.gaia.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 
 * 附件工具类,存储附件
 * @author lijian
 *
 */
public class AttachementUtil {
	
	private static File rootDir = new File(System.getProperty("user.dir"), "attachement");
	
	public static void storeAttachment(byte[] bytes, String path) {
		if(!rootDir.exists()){
			rootDir.mkdir(); 
		}
		File file = new File(rootDir + "/" + path);
		file.getParentFile().mkdirs();
		try (FileOutputStream fos = new FileOutputStream(file)){
			fos.write(bytes);
			fos.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] getAttachment(String path) {
		byte[] bytes = null;
		try (FileInputStream fis = new FileInputStream(rootDir + "/" + path)){
			bytes = new byte[fis.available()];
			fis.read(bytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return bytes;
	}
}
