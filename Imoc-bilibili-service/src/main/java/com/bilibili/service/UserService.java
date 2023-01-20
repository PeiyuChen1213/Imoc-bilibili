package com.bilibili.service;

import com.bilibili.dao.UserDao;
import com.bilibili.domain.User;
import com.bilibili.domain.UserConstant;
import com.bilibili.domain.UserInfo;
import com.bilibili.domain.exception.ConditionException;
import com.bilibili.service.util.MD5Util;
import com.bilibili.service.util.RSAUtil;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Chen Peiyu
 * @version 1.0
 * @description Service 接口
 * @date 1/20/2023 8:26 PM
 */

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public void addUser(User user) {
        //判断手机号是否为空，判断手机号是否已经注册过
        String phone = user.getPhone();
        if (StringUtils.isNullOrEmpty(phone)){
            throw new ConditionException("传入的手机号为空");
        }
        //判断手机号是否已经注册过 根据手机号查询
        User userByPhone = getUserByPhone(phone);
        if (userByPhone!=null){
            throw new ConditionException("该手机号已经注册过");
        }
        //加密注册时生成的密码
        Date now = new Date();
        String salt = String.valueOf(now.getTime());
        String password = user.getPassword();
        String rawPassword;
        try{
            rawPassword = RSAUtil.decrypt(password);
        }catch (Exception e){
            throw new ConditionException("密码解密失败！");
        }
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        user.setSalt(salt);
        user.setPassword(md5Password);
        user.setCreateTime(now);

        //然后将数据插入到数据库
        userDao.addUser(user);

        //在数据库里面添加用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setNick(UserConstant.DEFAULT_NICK);
        userInfo.setBirth(UserConstant.DEFAULT_BIRTH);
        userInfo.setGender(UserConstant.GENDER_MALE);
        userInfo.setCreateTime(now);
    }

    public User getUserByPhone(String phone){
        return userDao.getUserByPhone(phone);
    }

    public String login(User user){
        //先判断传入的手机号是不是为空的
        String phone = user.getPhone();
        if (StringUtils.isNullOrEmpty(phone)){
            throw new ConditionException("传入的手机号为空");
        }

        //查询数据库看看传入的手机号是不是已经注册过
        User dbUser = getUserByPhone(phone);
        if (dbUser!=null){
            throw new ConditionException("该手机号已经注册过");
        }

        // 然后开始解密
        String password = user.getPassword();
        String rawPassword;
        try{
            rawPassword = RSAUtil.decrypt(password);
        }catch (Exception e){
            throw new ConditionException("密码解密失败！");
        }
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        if(!md5Password.equals(dbUser.getPassword())){
            throw new ConditionException("密码错误！");
        }
        // 解密成功之后返回一个token给前端 进行校验
       // return TokenUtil.generateToken(dbUser.getId());

        return null;
    }
}
