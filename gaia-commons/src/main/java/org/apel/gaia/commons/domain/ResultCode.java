/**
 * bravo.org
 * Copyright (c) 2015-2018 All Rights Reserved.
 */
package org.apel.gaia.commons.domain;

/**
 * 结果码
 * <p>代表结果的码值，系统当中如遇到返回结果需要用固定码值代表时均可使用
 * <p>rpc接口返回的结果中可包含特定的结果码
 * @author lijian
 * @version $Id: ResultCode.java, v 0.1 2018年1月6日 下午12:56:14 lijian Exp $
 */
public abstract class ResultCode extends CompositeCode{

    private static final long serialVersionUID = 5778532410236447180L;

    /**
     * 构造方法。
     * 
     * @param codeString    结果码字符串-14位
     */
    public ResultCode(CompositeCodeString codeString) {
        super(codeString);
    }
    
    /**
     * 构造方法。
     * 
     * @param codeString  结果码字符串
     * @param systemName  结果码系统名称
     */
    public ResultCode(CompositeCodeString codeString, String systemName) {
        super(codeString, systemName);
    }
    
    /**
     * 构造方法。
     * 
     * @param codeString    结果码字符串
     * @param systemName    结果码系统名称
     * @param enumCode      结果码枚举值
     */
    public ResultCode(CompositeCodeString codeString, String systemName, String enumCode) {
        super(codeString, systemName, enumCode);
    }
    
    /**
     * 构造方法。
     * 
     * @param codeString    结果码字符串
     * @param systemName    结果码系统名称
     * @param enumCode      枚举值
     * @param codeAlias     结果码简称
     */
    public ResultCode(CompositeCodeString codeString, String systemName, String enumCode,
                     String codeAlias) {
        super(codeString, systemName, enumCode, codeAlias);
    }
    
    /**
     * 构造方法。
     * 
     * @param codeString    结果码字符串
     * @param systemName    结果码系统名称
     * @param enumCode      枚举值
     * @param codeAlias     结果码简称
     * @param description   结果码描述
     */
    public ResultCode(CompositeCodeString codeString, String systemName, String enumCode,
                     String codeAlias, String description) {
        super(codeString, systemName, enumCode, codeAlias, description);
    }

    /**
     * 构造方法。
     * 
     * @param codeLevel     结果码级别
     * @param codeType      结果码类型
     * @param systemCode    结果码系统编号
     */
    public ResultCode(String codeLevel, String codeType, String systemCode) {
        super(codeLevel, codeType, systemCode);
    }
    
    /**
     * 构造方法。
     * 
     * @param codeLevel     结果码级别
     * @param codeType      结果码类型
     * @param systemCode    结果码系统编号
     * @param systemName    结果码系统名称
     */
    public ResultCode(String codeLevel, String codeType, String systemCode, String systemName) {
        super(codeLevel, codeType, systemCode, systemName);
    }

    /**
     * 构造方法。
     * 
     * @param codeLevel     结果码级别
     * @param codeType      结果码类型
     * @param systemCode    结果码系统编号
     * @param systemName    结果码系统名称
     * @param enumCode      枚举值
     */
    public ResultCode(String codeLevel, String codeType, String systemCode, String systemName,
                     String enumCode) {
        super(codeLevel, codeType, systemCode, systemName, enumCode);
    }
    
    /**
     * 构造方法。
     * 
     * @param codeLevel     结果码级别
     * @param codeType      结果码类型
     * @param systemCode    结果码系统编号
     * @param systemName    结果码系统名称
     * @param enumCode      枚举值
     * @param codeAlias     结果码简称
     */
    public ResultCode(String codeLevel, String codeType, String systemCode, String systemName,
                     String enumCode, String codeAlias) {
        super(codeLevel, codeType, systemCode, systemName, enumCode, codeAlias);
    }
    
    /**
     * 构造方法。
     * 
     * @param codeLevel     结果码级别
     * @param codeType      结果码类型
     * @param systemCode    结果码系统编号
     * @param systemName    结果码系统名称
     * @param enumCode      枚举值
     * @param codeAlias     结果码简称
     * @param description   结果码描述
     */
    public ResultCode(String codeLevel, String codeType, String systemCode, String systemName,
                     String enumCode, String codeAlias, String description) {
        super(codeLevel, codeType, systemCode, systemName, enumCode, codeAlias, description);
    }
    
}
