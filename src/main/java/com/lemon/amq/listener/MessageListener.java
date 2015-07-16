package com.lemon.amq.listener;

/**
 * @ClassName: MessageListener
 * @Description: 消息监听类
 * @author matieli
 * @date 2014年10月23日 下午4:42:39
 * @company 随行付支付有限公司-架构设计部
 */
public abstract class MessageListener{
	
	public abstract void onMessage(String msg);

}
