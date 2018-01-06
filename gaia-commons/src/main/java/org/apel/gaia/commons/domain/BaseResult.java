/**
 * bravo.org
 * Copyright (c) 2015-2018 All Rights Reserved.
 */
package org.apel.gaia.commons.domain;

import java.io.Serializable;

import org.apel.gaia.commons.context.ErrorContext;

import lombok.Getter;
import lombok.Setter;

/**
 * 结果返回基类
 * 
 * <p>所有rpc接口的返回值都会返回结果基类的子类
 * <p>所有rpc接口中异常均被拦截，只会返回结果基类下的对象
 * 
 * @author lijian
 * @version $Id: BaseResult.java, v 0.1 2018年1月6日 下午3:18:39 lijian Exp $
 */
public abstract class BaseResult implements Serializable {

    private static final long serialVersionUID = -8520269513099293464L;

    @Setter
    @Getter
    /** 是否处理成功 */
    protected boolean success;
    
    @Setter
    @Getter
    /** 错误上下文 */
    protected ErrorContext errorContext = new ErrorContext();

    /**
     * 默认构造函数
     */
    public BaseResult() {}
    
    /**
     * 构造函数
     * 
     * @param success 处理结果
     * @param errorContext 错误上下文
     */
    public BaseResult(boolean success, ErrorContext errorContext) {
        this.success = success;
        this.errorContext = errorContext;
    }
    
    /**
     * 向错误上下文中添加错误对象
     * 
     * @param error 错误对象
     */
    public void addErrorCode(CommonError error) {
        this.errorContext.addError(error);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": [success=" + success + ", errorContext=" + errorContext + "]";
    }
    
}
