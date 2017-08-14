package org.apel.gaia.app.boot.multipart;

import java.text.DecimalFormat;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

public class FileUploadProgressListener implements ProgressListener{

	public final static String PROGRESS_SESSION = "progress";
	
	private HttpSession session;  
	  
    public FileUploadProgressListener(HttpSession session) {  
        this.session = session;  
    }  
    
    
	
	@Override
	public void update(long pBytesRead, long pContentLength, int pItems) {
		session.setAttribute(PROGRESS_SESSION, getPercent(pBytesRead, pContentLength));
	}
	
	private Integer getPercent(long pBytesRead, long pContentLength){
		DecimalFormat df = new DecimalFormat("#.##");
		double parseDouble = Double.parseDouble(df.format((double)pBytesRead/pContentLength)) * 100;
		DecimalFormat df2 = new DecimalFormat("#");
		String percent = df2.format(parseDouble);
		return Integer.valueOf(percent);
	}

}
