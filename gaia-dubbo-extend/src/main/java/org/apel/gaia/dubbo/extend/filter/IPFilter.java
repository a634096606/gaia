package org.apel.gaia.dubbo.extend.filter;

import java.io.File;
import java.io.FileInputStream;
import java.util.Objects;
import java.util.Properties;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;

/**
 * dubbo扩展点，ip白名单过滤
 * @author lijian
 *
 */
public class IPFilter implements Filter{
	
	private static Properties PROP;
	
	private static IP IP_CONTROLLER;
	
	static{
		PROP = new Properties();
		File file = new File(System.getProperty("user.dir") + "/config/ip.properties");
		if(file.exists()){
			try(FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/config/ip.properties")){
				PROP.load(fis);
				IP_CONTROLLER = IP.buildIP(PROP);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation)
			throws RpcException {
		String clientIp = RpcContext.getContext().getRemoteHost();
		if(!Objects.isNull(IP_CONTROLLER) && IP_CONTROLLER.isEnabled()){
			if(IP_CONTROLLER.getWhiteIps().contains(clientIp)){
				return invoker.invoke(invocation);
			}else{
				return null;
			}
		}else{
			return invoker.invoke(invocation);
		}
		
	}

}
