package com.bilibili.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bilibili.dao.UserMomentMapper;
import com.bilibili.domain.Constant.UserMomentsConstant;
import com.bilibili.domain.UserMoment;
import com.bilibili.service.util.RoctetMQUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * @author 29844
 * @description 针对表【t_user_moments(用户动态表)】的数据库操作Service
 * @createDate 2023-01-23 16:31:42
 */
@Service
public class UserMomentService {

    @Autowired
    private UserMomentMapper userMomentMapper;


    // 传入这个接口用来获取所有Bean对象 在这个类当中用于获取mq当中的生产者Bean 用于发送消息
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 添加用户动态
     *
     * @param userMoment 用户动态
     * @throws Exception
     */

    public void addUserMoments(UserMoment userMoment) throws Exception {
        userMoment.setCreateTime(new Date());
        userMomentMapper.addUserMoments(userMoment);
        //将新增的动态信息根据rocketmq发送到粉丝
        //获取生产者
        DefaultMQProducer momentsProducer = (DefaultMQProducer) applicationContext.getBean("momentsProducer");
        //创建新的消息
        //将topic和发送的内容转成字符数组封装成一个message对象
        Message message = new Message(UserMomentsConstant.TOPIC_MOMENTS, JSONObject.toJSONString(userMoment).getBytes(StandardCharsets.UTF_8));
        //调用方法，将信息发送出去
        RoctetMQUtil.syncSendMsg(momentsProducer, message);

    }


    /**
     * 根据当前用户id 获取用户动态
     *
     * @param userId
     * @return
     */
    public List<UserMoment> getUserSubscribedMoments(Long userId) {
        String key = "subscribed-" + userId;
        String listStr = redisTemplate.opsForValue().get(key);
        return JSONArray.parseArray(listStr, UserMoment.class);
    }
}
