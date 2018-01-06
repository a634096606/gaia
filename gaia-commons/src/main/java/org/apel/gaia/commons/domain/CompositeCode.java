/**
 * bravo.org
 * Copyright (c) 2015-2018 All Rights Reserved.
 */
package org.apel.gaia.commons.domain;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统综合码，抽象类，子类包含结果码和异常码。
 * <p>综合码码位总共14位，不多不少，必须满足；
 * <p>系统公用码的格式如下：
 *      <table border="1">
 *          <tr>
 *           <td><b>位置</b></td><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td><td>8</td><td>9</td><td>10</td><td>11</td><td>12</td><td>13</td>
 *          </tr>
 *          <tr>
 *           <td><b>示例</b></td><td>S</td><td>Y</td><td>S</td><td>M</td><td>_</td><td>M</td><td>1</td><td>_</td><td>1</td><td>0</td><td>0</td><td>0</td><td>1</td>
 *          </tr>
 *          <tr>
 *           <td><b>说明</b></td><td colspan=4>固定标识1</td><td>分<br>隔<br>符<br></td><td colspan=2>固定<br>标识2</td><td>分<br>隔<br>符<br></td><td>返<br>回<br>码<br>级<br>别</td><td>返<br>回<br>码<br>类<br>型</td><td colspan=3>系统编号</td>
 *          </tr>
 *      </table>
 *   
 * 
 * @author lijian
 * @version $Id: CommonCode.java, v 0.1 2018年1月6日 下午12:34:15 lijian Exp $
 */
public abstract class CompositeCode implements Serializable{

    private static final long serialVersionUID = 2377988816412754062L;

    @Getter
    @Setter
    /** 综合码固定前缀 ，可以进行变更，通常固定码指代的是一个大的应用或者一个大的平台*/
    protected String prefix           = "SYSM_M1_";

    @Setter
    @Getter
    /** 综合码级别[第9位]，参见<code>ResultCodeLevel</code> */
    protected String                codeLevel;

    @Setter
    @Getter
    /** 综合码类型[第10位]，参见<code>ResultCodeType</code> */
    protected String                codeType;

    @Setter
    @Getter
    /** 系统编号[第11-13位]，通常指代的是一个微服务(类似用户中心、订单中心)编号，参见<code>SystemCode</code> */
    protected String                systemCode;

    @Setter
    @Getter
    /** 与系统编号与之呼应的系统名称 ，可空*/
    protected String                systemName;

    @Setter
    @Getter
    /** 枚举码，综合码通常在使用时都会作为枚举类的内部属性，所以会包含一个枚举值，可空*/
    protected String                enumCode;

    @Setter
    @Getter
    /** 综合码的简称，可空 */
    protected String                codeAlias;

    @Setter
    @Getter
    /** 综合码的描述，错误时描述错误，结果表述时描述结果，可空 */
    protected String                description;

    // ~~~ 构造方法

    /**
     * 构造方法。
     * 
     * @param codeString    综合码字符串-14位
     */
    public CompositeCode(CompositeCodeString codeString) {

        //综合码长度检查
        checkStringLength(codeString.toString(), 14);

        //拆分综合码
        spliteResultCode(codeString.toString());
    }

    /**
     * 构造方法。
     * 
     * @param codeString  综合码字符串
     * @param systemName  综合码系统名称
     */
    public CompositeCode(CompositeCodeString codeString, String systemName) {
        this(codeString);
        this.systemName = systemName;
    }

    /**
     * 构造方法。
     * 
     * @param codeString    综合码字符串
     * @param systemName    综合码系统名称
     * @param enumCode      综合码枚举值
     */
    public CompositeCode(CompositeCodeString codeString, String systemName, String enumCode) {
        this(codeString, systemName);
        this.enumCode = enumCode;
    }

    /**
     * 构造方法。
     * 
     * @param codeString    综合码字符串
     * @param systemName    综合码系统名称
     * @param enumCode      枚举值
     * @param codeAlias     综合码简称
     */
    public CompositeCode(CompositeCodeString codeString, String systemName, 
                         String enumCode, String codeAlias) {
        this(codeString, systemName, enumCode);
        this.codeAlias = codeAlias;
    }

    /**
     * 构造方法。
     * 
     * @param codeString    综合码字符串
     * @param systemName    综合码系统名称
     * @param enumCode      枚举值
     * @param codeAlias     综合码简称
     * @param description   综合码描述
     */
    public CompositeCode(CompositeCodeString codeString, String systemName, 
                         String enumCode, String codeAlias,
                         String description) {
        this(codeString, systemName, enumCode, codeAlias);
        this.description = description;
    }
    
    /**
     * 构造方法。
     * 
     * @param codeLevel     综合码级别
     * @param codeType      综合码类型
     * @param systemCode    综合码系统编号
     */
    public CompositeCode(String codeLevel, String codeType, String systemCode) {
        this.codeLevel = codeLevel;
        this.codeType = codeType;
        this.systemCode = systemCode;
    }
    
    /**
     * 构造方法。
     * 
     * @param codeLevel     综合码级别
     * @param codeType      综合码类型
     * @param systemCode    综合码系统编号
     * @param systemName    综合码系统名称
     */
    public CompositeCode(String codeLevel, String codeType, 
                         String systemCode, String systemName) {
        this(codeLevel, codeType, systemCode);
        this.systemName = systemName;
    }
    
    /**
     * 构造方法。
     * 
     * @param codeLevel     综合码级别
     * @param codeType      综合码类型
     * @param systemCode    综合码系统编号
     * @param systemName    综合码系统名称
     * @param enumCode      枚举值
     */
    public CompositeCode(String codeLevel, String codeType, 
                         String systemCode, String systemName,
                         String enumCode) {
        this(codeLevel, codeType, systemCode, systemName);
        this.enumCode = enumCode;
    }
    
    /**
     * 构造方法。
     * 
     * @param codeLevel     综合码级别
     * @param codeType      综合码类型
     * @param systemCode    综合码系统编号
     * @param systemName    综合码系统名称
     * @param enumCode      枚举值
     * @param codeAlias     综合码简称
     */
    public CompositeCode(String codeLevel, String codeType, 
                         String systemCode, String systemName,
                         String enumCode, String codeAlias) {
        this(codeLevel, codeType, systemCode, systemName, enumCode);
        this.codeAlias = codeAlias;
    }
    
    /**
     * 构造方法。
     * 
     * @param codeLevel     综合码级别
     * @param codeType      综合码类型
     * @param systemCode    综合码系统编号
     * @param systemName    综合码系统名称
     * @param enumCode      枚举值
     * @param codeAlias     综合码简称
     * @param description   综合码描述
     */
    public CompositeCode(String codeLevel, String codeType, 
                         String systemCode, String systemName,
                         String enumCode, String codeAlias,
                         String description) {
        this(codeLevel, codeType, systemCode, systemName, enumCode, codeAlias);
        this.description = description;
    }

    // ~~~ 公有方法

    /**
     * 组装返回码字符串。
     * 
     * @return  返回码字符串
     */
    public String fetchResultCode() {
        StringBuffer sb = new StringBuffer();

        sb.append(prefix);
        sb.append(codeLevel);
        sb.append(codeType);
        sb.append(systemCode);

        return sb.toString();
    }

    @Override
    public String toString() {

        //错误完整性检查
        checkStringLength(this.codeLevel, 1);
        checkStringLength(this.codeType, 1);
        checkStringLength(this.systemCode, 3);

        //组装综合码字符串
        String resultCode = fetchResultCode();

        return resultCode;
    }

    /** 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.fetchResultCode().hashCode();
    }

    /** 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        return StringUtils.equals(this.fetchResultCode(), (((CompositeCode) obj).fetchResultCode()));
    }

    // ~~~ 内部方法

    /**
     * 解析和拆分综合码。
     * 
     * @param resultCode    综合码字符串
     */
    private void spliteResultCode(String resultCode) {
        if (!resultCode.startsWith(prefix)) {
            throw new IllegalArgumentException();
        }

        char[] chars = resultCode.toCharArray();

        this.codeLevel = "" + chars[8];
        this.codeType = "" + chars[9];
        this.systemCode = "" + chars[10] + chars[11] + chars[12];
    }

    /**
     * 字符串长度检查。
     * 
     * @param resultCode    综合码字符串
     * @param length        长度
     */
    private void checkStringLength(String codeString, int length) {
        if (codeString == null || codeString.length() != length) {
            throw new IllegalArgumentException();
        }
    }
    
}
