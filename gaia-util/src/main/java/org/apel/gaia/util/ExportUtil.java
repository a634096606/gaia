package org.apel.gaia.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ExportUtil {

	private static Logger LOG = LoggerFactory.getLogger(ExportUtil.class);
	
	private static final String CONTENT_DISPOSITION = "Content-Disposition";
	
	/**
	 * 构建服务器传输给客户端的二进制件信息
	 * @param fileName 文件名为空则显示当前时间
	 * @param resource  word 文件的路径
	 * @return
	 * @throws Exception
	 */
	public static ResponseEntity<byte[]> getResponseEntityByFile(File file,String fileName) throws RuntimeException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return getResponseEntityByFile(fis, fileName);
	}
	
	
	public static ResponseEntity<byte[]> getResponseEntityByFile(FileInputStream stream, String fileName) throws RuntimeException {
        byte[] cbyte = null;
		try {
			cbyte = IOUtils.toByteArray(stream);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally{
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
        return getResponseEntityByFile(cbyte, fileName);
	}
	
	public static ResponseEntity<byte[]> getResponseEntityByFile(byte[] cbyte,String fileName) throws RuntimeException {
		HttpHeaders responseHeaders = new HttpHeaders();
    	responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    	responseHeaders.setContentLength(cbyte.length);
    	try {
			responseHeaders.set(CONTENT_DISPOSITION, "attachment;filename=\""+URLEncoder.encode(fileName,"UTF-8")+"\"");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			LOG.info("导出文件名称URL编码异常");
			throw new RuntimeException(e);
		}
		return new ResponseEntity<byte[]>(cbyte, responseHeaders, HttpStatus.OK);
	}
	
}
