package com.bilibili.service.util;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Chen Peiyu
 * @version 1.0
 * @description rocketMq 的工具类
 * @date 1/23/2023 4:36 PM
 */

public class RoctetMQUtil {

    public static void syncSendMsg(DefaultMQProducer defaultMQProducer, Message message) throws Exception{
        SendResult result = defaultMQProducer.send(message);
        System.out.println(result);
    }

    public static void asyncSendMsg(DefaultMQProducer defaultMQProducer, Message message) throws Exception{
        int messageCount = 2;
        CountDownLatch2 countDownLatch = new CountDownLatch2(messageCount);
        for (int i = 0; i <messageCount ; i++) {
            defaultMQProducer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    countDownLatch.countDown();
                    System.out.println(sendResult.getMsgId());
                }

                @Override
                public void onException(Throwable throwable) {
                    countDownLatch.countDown();
                    System.out.println("发送消息的时候出现了异常!"+throwable);;
                    throwable.printStackTrace();
                }

            });
        }

        countDownLatch.await(5, TimeUnit.SECONDS);
    }
}
