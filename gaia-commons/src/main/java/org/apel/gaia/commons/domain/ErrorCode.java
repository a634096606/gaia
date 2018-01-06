/**
 * bravo.org
 * Copyright (c) 2015-2018 All Rights Reserved.
 */
package org.apel.gaia.commons.domain;

/**
 * 错误码
 * <p>代表系统当中所有发生异常时的码值
 * <p>系统当中如果发生异常，尽量使用错误码表示
 * @author lijian
 * @version $Id: ErrorCode.java, v 0.1 2018年1月6日 下午12:59:19 lijian Exp $
 */
public class ErrorCode extends CompositeCode{

    private static final long serialVersionUID = -1030715819959379599L;

    /**
     * 构造方法。
     * 
     * @param codeString    错误码字符串-14位
     */
    public ErrorCode(CompositeCodeString codeString) {
        super(codeString);
    }
    
    /**
     * 构造方法。
     * 
     * @param codeString  错误码字符串
     * @param systemName  错误码系统名称
     */
    public ErrorCode(CompositeCodeString codeString, String systemName) {
        super(codeString, systemName);
    }
    
    /**
     * 构造方法。
     * 
     * @param codeString    错误码字符串
     * @param systemName    错误码系统名称
     * @param enumCode      错误码枚举值
     */
    public ErrorCode(CompositeCodeString codeString, String systemName, String enumCode) {
        super(codeString, systemName, enumCode);
    }
    
    /**
     * 构造方法。
     * 
     * @param codeString    错误码字符串
     * @param systemName    错误码系统名称
     * @param enumCode      枚举值
     * @param codeAlias     错误码简称
     */
    public ErrorCode(CompositeCodeString codeString, String systemName, String enumCode,
                     String codeAlias) {
        super(codeString, systemName, enumCode, codeAlias);
    }
    
    /**
     * 构造方法。
     * 
     * @param codeString    错误码字符串
     * @param systemName    错误码系统名称
     * @param enumCode      枚举值
     * @param codeAlias     错误码简称
     * @param description   错误码描述
     */
    public ErrorCode(CompositeCodeString codeString, String systemName, String enumCode,
                     String codeAlias, String description) {
        super(codeString, systemName, enumCode, codeAlias, description);
    }

    /**
     * 构造方法。
     * 
     * @param codeLevel     错误码级别
     * @param codeType      错误码类型
     * @param systemCode    错误码系统编号
     */
    public ErrorCode(String codeLevel, String codeType, String systemCode) {
        super(codeLevel, codeType, systemCode);
    }
    
    /**
     * 构造方法。
     * 
     * @param codeLevel     错误码级别
     * @param codeType      错误码类型
     * @param systemCode    错误码系统编号
     * @param systemName    错误码系统名称
     */
    public ErrorCode(String codeLevel, String codeType, String systemCode, String systemName) {
        super(codeLevel, codeType, systemCode, systemName);
    }

    /**
     * 构造方法。
     * 
     * @param codeLevel     错误码级别
     * @param codeType      错误码类型
     * @param systemCode    错误码系统编号
     * @param systemName    错误码系统名称
     * @param enumCode      枚举值
     */
    public ErrorCode(String codeLevel, String codeType, String systemCode, String systemName,
                     String enumCode) {
        super(codeLevel, codeType, systemCode, systemName, enumCode);
    }
    
    /**
     * 构造方法。
     * 
     * @param codeLevel     错误码级别
     * @param codeType      错误码类型
     * @param systemCode    错误码系统编号
     * @param systemName    错误码系统名称
     * @param enumCode      枚举值
     * @param codeAlias     错误码简称
     */
    public ErrorCode(String codeLevel, String codeType, String systemCode, String systemName,
                     String enumCode, String codeAlias) {
        super(codeLevel, codeType, systemCode, systemName, enumCode, codeAlias);
    }
    
    /**
     * 构造方法。
     * 
     * @param codeLevel     错误码级别
     * @param codeType      错误码类型
     * @param systemCode    错误码系统编号
     * @param systemName    错误码系统名称
     * @param enumCode      枚举值
     * @param codeAlias     错误码简称
     * @param description   错误码描述
     */
    public ErrorCode(String codeLevel, String codeType, String systemCode, String systemName,
                     String enumCode, String codeAlias, String description) {
        super(codeLevel, codeType, systemCode, systemName, enumCode, codeAlias, description);
    }

}
