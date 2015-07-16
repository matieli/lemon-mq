package com.lemon.amq.producer;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lemon.amq.exception.InitException;
import com.lemon.amq.util.MessageUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @ClassName: MessageProducer
 * @Description: 消息提供类
 * @author matieli
 * @date 2014年10月23日 下午4:42:04
 * @company 随行付支付有限公司-架构设计部
 */
public class MessageProducer {
	static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);
	String host;
	String exchangeName;
	String producerMessageType;
	
	private static ExecutorService executorService=Executors.newCachedThreadPool();
	
	
	public void stop(){
		executorService.shutdown();
	}
	public void sendMessage(final String message){
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				sendMessage1(message);
			}
		});
	}
	private Connection getConnection() throws IOException {
		ConnectionFactory connFac = new ConnectionFactory();
		connFac.setHost(host);
		Connection conn=connFac.newConnection();
		return conn;
	}
	
	private  void sendMessage1(String message){
		if (MessageUtil.isNullOrEmpty(getExchangeName())) {
			throw new InitException("in MessageProducer,[exchangeName] must not null.");
		}
		if(MessageUtil.isNullOrEmpty(getHost())){
			throw new InitException("in MessageProducer,[host] must be not null.");
		}
		if(MessageUtil.isNullOrEmpty(getProducerMessageType())){
			throw new InitException("in MessageProducer,[messageType] must be not null.");
		}
		Channel channel = null;
		Connection conn =null;
		try {
			conn=getConnection();
			channel = conn.createChannel();
			channel.exchangeDeclare(exchangeName, "topic");
			channel.basicPublish(exchangeName, producerMessageType, null,message.getBytes());
			logger.info("send message["+message+"]");
		} catch (IOException e) {
			logger.error("create chanel Connection error,due to "+e.getMessage());
		} catch (InitException e) {
			logger.error("InitException ,due to "+e.getMessage());
		} catch (Exception e) {
			logger.error("Exception ,due to "+e.getMessage());
		}finally{
			try {
				if(channel!=null && channel.isOpen()){
					channel.close();
				}
			} catch (Exception e1) {
				logger.warn("channel close Exception ,due to "+e1.getMessage());
			}
			if (conn != null && conn.isOpen()) {
				try {
					conn.close();
				} catch (Exception e) {
					logger.warn("Connection close Exception ,due to "+e.getMessage());
				}
			}
		}
	
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public String getProducerMessageType() {
		return producerMessageType;
	}

	public void setProducerMessageType(String producerMessageType) {
		this.producerMessageType = producerMessageType;
	}
}
