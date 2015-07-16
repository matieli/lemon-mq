package com.lemon.amq.consumer;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lemon.amq.exception.InitException;
import com.lemon.amq.listener.MessageListener;
import com.lemon.amq.util.MessageUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * @ClassName: MessageConsumer
 * @Description: 消息消费类
 * @author matieli
 * @date 2014年10月23日 下午4:41:45
 * @company 随行付支付有限公司-架构设计部
 */
public class MessageConsumer {
	
	static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
	String host;
	String exchangeName;
	String consumerMessageType;
	
	MessageListener listener;
	
	@PostConstruct
	public void start() {
		MyThread th=new MyThread();
		th.setName("cmq");
		th.setDaemon(true);
		th.start();
	}
	class MyThread extends Thread{
		@Override
		public void run() {
			onConsumer();
		}
		
	}
	
	public void init(){
		if (MessageUtil.isNullOrEmpty(getExchangeName())) {
			throw new InitException("in MessageConsumer,[exchangeName] must not null.");
		}
		if(getListener()==null){
			throw new InitException("in MessageConsumer,[listener] must be implemented.");
		}
		if(MessageUtil.isNullOrEmpty(getHost())){
			throw new InitException("in MessageConsumer,[host] must be not null.");
		}
		if(MessageUtil.isNullOrEmpty(getConsumerMessageType())){
			throw new InitException("in MessageConsumer,[messageType] must be not null.");
		}
		logger.info("MessageConsumer 加载");
	}

	private Connection getConnection() throws IOException {
		ConnectionFactory connFac = new ConnectionFactory();
		connFac.setHost(host);
		return connFac.newConnection();
	}

	public void onConsumer() {
		
		Channel channel;
		try {
			init();
			channel = getConnection().createChannel();
			channel.exchangeDeclare(exchangeName, "topic");
			String queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, exchangeName, consumerMessageType);

			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, true, consumer);

			while (true) {
				Delivery delivery = consumer.nextDelivery();
				String msg = new String(delivery.getBody());
				logger.info("received message[" + msg + "] from "+ exchangeName);
				listener.onMessage(msg);
			}
		} catch (IOException e) {
			logger.error("create channel error,due to " + e.getMessage());
		} catch (ShutdownSignalException e) {
			logger.error("connection to AMQP broker is Shutdown,due to "+ e.getMessage());
		} catch (ConsumerCancelledException e) {
			logger.error("ConsumerCancelledException,due to " + e.getMessage());
		} catch (InterruptedException e) {
			logger.error("InterruptedException,due to " + e.getMessage());
		}catch (InitException e) {
			logger.error("InitException,due to " + e.getMessage());
		}catch (Exception e) {
			logger.error("Exception,due to " + e.getMessage());
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
	public MessageListener getListener() {
		return listener;
	}
	public void setListener(MessageListener listener) {
		this.listener = listener;
	}
	public String getConsumerMessageType() {
		return consumerMessageType;
	}
	public void setConsumerMessageType(String consumerMessageType) {
		this.consumerMessageType = consumerMessageType;
	}
	
}
