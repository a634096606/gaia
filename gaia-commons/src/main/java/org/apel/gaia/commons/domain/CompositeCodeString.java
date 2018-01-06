/**
 * bravo.org
 * Copyright (c) 2015-2018 All Rights Reserved.
 */
package org.apel.gaia.commons.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 综合码字符串类
 * <p>用类来表示一个综合码字符串，方便传参
 * 
 * @author lijian
 * @version $Id: CompositeCodeString.java, v 0.1 2018年1月6日 下午2:27:29 lijian Exp $
 */
@Setter
@Getter
public class CompositeCodeString {

    private String codeString;
    
    public String toString() {
        return codeString;
    }
    
}
