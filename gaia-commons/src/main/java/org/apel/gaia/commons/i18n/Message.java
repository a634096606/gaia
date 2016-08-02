package org.apel.gaia.commons.i18n;

import java.io.Serializable;

/**
 * 返回给客户端的消息信息
 * @author lijian
 *
 */
public class Message implements Serializable {
	private static final long serialVersionUID = 8117244181993856881L;

	// 响应状态码，一般自定义
	private int statusCode;
	// 响应的状态文本信息
	private String statusText;
	// 响应的数据信息
	private Object data;

	public Message() {
	}

	public Message(int statusCode, String statusText) {
		this(statusCode, statusText, null);
	}

	public Message(int statusCode, String statusText, Object data) {
		this.statusCode = statusCode;
		this.statusText = statusText;
		this.data = data;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
