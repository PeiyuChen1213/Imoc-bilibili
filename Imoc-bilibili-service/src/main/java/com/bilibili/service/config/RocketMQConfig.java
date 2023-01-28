package com.bilibili.service.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bilibili.domain.Constant.UserMomentsConstant;
import com.bilibili.domain.UserFollowing;
import com.bilibili.domain.UserMoment;
import com.bilibili.service.UserFollowingService;
import io.netty.util.internal.StringUtil;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen Peiyu
 * @version 1.0
 * @description mq 的配置类
 * @date 1/23/2023 3:40 PM
 */
@Configuration
public class RocketMQConfig {

    @Value("${rocketmq.name.server.address}")
    private String nameServerAddr;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Autowired
    private UserFollowingService userFollowingService;


    /**
     * 创建一个动态生产者
     */
    //加入到spring管理
    @Bean("momentsProducer")
    public DefaultMQProducer momentsProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(UserMomentsConstant.GROUP_MOMENTS);
        producer.setNamesrvAddr(nameServerAddr);
        producer.start();
        return producer;
    }

    //消费者
    @Bean("momentsConsumer")
    public DefaultMQPushConsumer momentsConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(UserMomentsConstant.GROUP_MOMENTS);
        consumer.setNamesrvAddr(nameServerAddr);
        //订阅一个主题
        consumer.subscribe(UserMomentsConstant.TOPIC_MOMENTS, "*");
        //注册监听器
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            // 监听方法 拿到生产者的消息之后进行处理
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                // 取出消息
                MessageExt msg = msgs.get(0);
                if (msg == null) {
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                //消息不为空
                //将内容取出来
                String bodyStr = new String(msg.getBody());
                // 将传递的信息转成一个Usermoment的java类
                UserMoment userMoment = JSONObject.toJavaObject(JSONObject.parseObject(bodyStr), UserMoment.class);
                //获取当前登陆的userId
                Long userId = userMoment.getUserId();
                // 获取当前的用户粉丝
                List<UserFollowing> fanList = userFollowingService.getUserFans(userId);
                // 遍历粉丝列表，将消息内容发送到所有的粉丝
                for (UserFollowing fan : fanList) {
                    //将消息内容存放在redis的数据库当中
                    String key = "subscribed-" + fan.getUserId();
                    //如果其他用户也发送动态，需要先获取其他用户的动态然后再加入 该用户传入的动态 再set到redis数据库里面
                    String subscribedListStr = redisTemplate.opsForValue().get(key);
                    //创建一个储存消息内容的列表
                    List<UserMoment> subscribedList;
                    if (StringUtil.isNullOrEmpty(subscribedListStr)) {
                        subscribedList = new ArrayList<>();
                    } else {
                        subscribedList = JSONArray.parseArray(subscribedListStr, UserMoment.class);
                    }
                    subscribedList.add(userMoment);
                    redisTemplate.opsForValue().set(key, JSONObject.toJSONString(subscribedList));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        return consumer;
    }


}
