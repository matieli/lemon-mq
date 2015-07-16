/**
 * @Title: InitException.java
 * @Package com.lemon.amq.exception
 * @Description: TODO(用一句话描述该文件做什么)
 * @author matieli
 * @date 2014年10月28日 下午2:08:54
 * @company 随行付支付有限公司-架构设计部
 */
package com.lemon.amq.exception;

/**
 * @ClassName: InitException
 * @Description: 初始化异常
 * @author matieli
 * @date 2014年10月28日 下午2:08:54
 * @company 随行付支付有限公司-架构设计部
 */
public class InitException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param cause
	 */
	public InitException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 */
	public InitException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 * @param message
	 */
	public InitException(String message, Throwable cause) {
		super(message, cause);
	}

}
