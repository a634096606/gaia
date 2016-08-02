package org.apel.gaia.commons.exception;


/**
 * 平台异常类
 * @author lijian
 *
 */
public abstract class PlatformException extends RuntimeException {

	private static final long serialVersionUID = -5143695406381565749L;
	
	public PlatformException() {}
   
	
	public PlatformException(Exception e) {
		 super(e);
    }

	public PlatformException(String msg) {
		super(msg);
	}

	public PlatformException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	@Override
	public String getMessage() {
		return buildMessage(super.getMessage(), getCause());
	}
	
	/**
	 * Build a message for the given base message and root cause.
	 * @param message the base message
	 * @param cause the root cause
	 * @return the full exception message
	 */
	public static String buildMessage(String message, Throwable cause) {
		if (cause != null) {
			StringBuilder sb = new StringBuilder();
			if (message != null) {
				sb.append(message).append("; ");
			}
			sb.append("nested exception is ").append(cause);
			return sb.toString();
		}
		else {
			return message;
		}
	}
	
}
