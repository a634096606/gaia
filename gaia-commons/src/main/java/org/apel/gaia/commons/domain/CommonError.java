/**
 * bravo.org
 * Copyright (c) 2015-2018 All Rights Reserved.
 */
package org.apel.gaia.commons.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 标准错误类
 * <p>包含内容：
 * <ul>
 *  <li>错误码</li>
 *  <li>错误产生系统</li>
 * </ul>
 * 
 * @author lijian
 * @version $Id: CommonError.java, v 0.1 2018年1月6日 下午10:19:41 lijian Exp $
 */
@Setter
@Getter
public class CommonError implements Serializable {

    private static final long serialVersionUID = -7800943609843200324L;

    /** 错误码 */
    private ErrorCode errorCode;
    
    /** 错误产生系统 */
    private String location;
    
    /**
     * 默认构造方法
     */
    public CommonError() {
        super();
    }
    
    /**
     * 默认构造方法
     * @param errorCode 错误码
     * @param location  错误产生系统
     */
    public CommonError(ErrorCode errorCode, String location) {
        this.errorCode = errorCode;
        this.location = location;
    }

    // ~~~ 重写方法
    
    @Override
    public String toString() {
        return errorCode + "@" + location + "::" + errorCode.getDescription();
    }
    
}
