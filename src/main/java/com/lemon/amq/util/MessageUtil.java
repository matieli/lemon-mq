/**
 * @Title: MessageUtil.java
 * @Package com.lemon.amq.util
 * @Description: TODO(用一句话描述该文件做什么)
 * @author matieli
 * @date 2014年10月24日 上午10:45:59
 * @company 随行付支付有限公司-架构设计部
 */
package com.lemon.amq.util;

/**
 * @ClassName: MessageUtil
 * @Description: 消息工具类
 * @author matieli
 * @date 2014年10月24日 上午10:45:59
 * @company 随行付支付有限公司-架构设计部
 */
public final class MessageUtil {
	public static boolean isNullOrEmpty(String value) {
		if (value == null)
			return true;
		value = value.trim();
		if (value.equals(""))
			return true;
		return false;
	}
}
