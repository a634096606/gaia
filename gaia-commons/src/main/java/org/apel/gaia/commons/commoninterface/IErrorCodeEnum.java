/**
 * bravo.org
 * Copyright (c) 2015-2018 All Rights Reserved.
 */
package org.apel.gaia.commons.commoninterface;

import org.apel.gaia.commons.domain.ErrorCode;

/**
 * 
 * @author lijian
 * @version $Id: IErrorCodeEnum.java, v 0.1 2018年1月6日 下午1:08:40 lijian Exp $
 */
public interface IErrorCodeEnum extends ICompisteCodeNum {

    public ErrorCode getCode();
    
}
