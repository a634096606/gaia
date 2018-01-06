/**
 * bravo.org
 * Copyright (c) 2015-2018 All Rights Reserved.
 */
package org.apel.gaia.commons.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apel.gaia.commons.domain.CommonError;
import org.apel.gaia.commons.domain.ErrorCode;

import lombok.Getter;
import lombok.Setter;

/**
 * 错误上下文对象
 * 
 * <p>内容包含有：标准错误堆栈
 * 
 * @author lijian
 * @version $Id: ErrorContext.java, v 0.1 2018年1月6日 下午10:27:59 lijian Exp $
 */
public class ErrorContext implements Serializable {

    private static final long serialVersionUID = -2191953263464121647L;

    @Setter
    @Getter
    private List<CommonError>   errorStack       = new ArrayList<>();

    private static final String SPLIT            = "|";
    
    /**
     * 默认构造方法
     */
    public ErrorContext() {}
    
    /**
     * 获取当前标准错误对象
     * @return 标准错误对象
     */
    public CommonError fetchCurrentError() {
        
        if (errorStack != null && errorStack.size() > 0) {
            
            return errorStack.get(errorStack.size() - 1);
        }
        return null;
    }
    
    /**
     * 获取原始标准错误对象
     * @return 标准错误对象
     */
    public CommonError fetchRootError() {
        
        if (errorStack != null && errorStack.size() > 0) {
            return errorStack.get(0);
        }
        return null;
    }
    
    /**
     * 获取当前标准错误码 
     * @return 准错误码 
     */
    public ErrorCode fetchCurrentErrorCode() {
        
        if (errorStack != null && errorStack.size() > 0) {
            
            return errorStack.get(errorStack.size() - 1).getErrorCode();
        }
        return null;
    }
    
    /**
     * 向错误堆栈中添加错误对象
     * 
     * @param error 错误对象
     */
    public void addError(CommonError error) {
        
        if (errorStack == null) {
            
            errorStack = new ArrayList<>();
        }
        errorStack.add(error);
    }
    
    // ~~~重写方法
    /** 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        
        StringBuffer sb = new StringBuffer();
        
        for (int i = errorStack.size(); i > 0; i--) {
            
            if (i == errorStack.size()) {
                
                sb.append(errorStack.get(i - 1));
            } else {
                
                sb.append(SPLIT).append(errorStack.get(i - 1));
            }
        }
        return sb.toString();
    }
    
    
}
