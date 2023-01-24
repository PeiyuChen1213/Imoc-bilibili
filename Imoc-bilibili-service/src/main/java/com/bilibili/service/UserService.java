package com.bilibili.service;

import com.alibaba.fastjson.JSONObject;
import com.bilibili.dao.UserMapper;
import com.bilibili.domain.Constant.UserConstant;
import com.bilibili.domain.PageResult;
import com.bilibili.domain.RefreshTokenDetail;
import com.bilibili.domain.User;
import com.bilibili.domain.UserInfo;
import com.bilibili.domain.exception.ConditionException;
import com.bilibili.service.util.MD5Util;
import com.bilibili.service.util.RSAUtil;
import com.bilibili.service.util.TokenUtil;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Chen Peiyu
 * @version 1.0
 * @description Service 接口
 * @date 1/20/2023 8:26 PM
 */

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;


    // 引入这样的一个依赖
    @Autowired
    private UserAuthService userAuthService;

    public void addUser(User user) {
        //判断手机号是否为空，判断手机号是否已经注册过
        String phone = user.getPhone();
        if (StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("传入的手机号为空");
        }
        //判断手机号是否已经注册过 根据手机号查询
        User userByPhone = getUserByPhone(phone);
        if (userByPhone != null) {
            throw new ConditionException("该手机号已经注册过");
        }
        //加密注册时生成的密码
        Date now = new Date();
        String salt = String.valueOf(now.getTime());
        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败！");
        }
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        user.setSalt(salt);
        user.setPassword(md5Password);
        user.setCreateTime(now);

        //然后将数据插入到数据库
        userMapper.addUser(user);

        //在数据库里面添加用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setNick(UserConstant.DEFAULT_NICK);
        userInfo.setBirth(UserConstant.DEFAULT_BIRTH);
        userInfo.setGender(UserConstant.GENDER_MALE);
        userInfo.setCreateTime(now);

        //将用户信息表数据插入到数据库当中
        userMapper.addUserInfo(userInfo);

        //添加一个默认的角色
        userAuthService.addUserDefaultRole(user.getId());

    }

    public User getUserByPhone(String phone) {
        return userMapper.getUserByPhone(phone);
    }

    public String login(User user) throws Exception {
        //先判断传入的手机号是不是为空的
        String phone = user.getPhone();
        if (StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("传入的手机号为空");
        }

        //查询数据库看看传入的手机号是不是已经注册过
        User dbUser = getUserByPhone(phone);
        if (dbUser == null) {
            throw new ConditionException("该用户不存在!");
        }

        // 然后开始解密
        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败！");
        }
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        if (!md5Password.equals(dbUser.getPassword())) {
            throw new ConditionException("密码错误！");
        }
        // 解密成功之后返回一个token给前端 进行校验 这个时候没有任何异常的出现
        return TokenUtil.generateAccessToken(dbUser.getId());
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param currentUserId
     * @return
     */
    public User getUserInfo(Long currentUserId) {
        User user = userMapper.getUserById(currentUserId);
        UserInfo userInfo = userMapper.getUserInfoById(currentUserId);
        user.setUserInfo(userInfo);
        return user;
    }

    public Integer updateUsers(User user) throws Exception {
        // 获取需要修改的用户的id
        Long id = user.getId();
        //判断数据当中是否有这个数据
        User dbUser = userMapper.getUserById(id);
        if (dbUser == null) {
            throw new ConditionException("用户不存在！");
        }
        //如果要修改密码需要先进行解密
        if (!StringUtils.isNullOrEmpty(user.getPassword())) {
            String rawPassword = RSAUtil.decrypt(user.getPassword());
            String md5Password = MD5Util.sign(rawPassword, dbUser.getSalt(), "UTF-8");
            user.setPassword(md5Password);
        }
        user.setUpdateTime(new Date());
        return userMapper.updateUsers(user);
    }

    public Integer updateUserInfos(UserInfo userInfo) {
        userInfo.setUpdateTime(new Date());
        return userMapper.updateUserInfos(userInfo);
    }

    public User getUserById(Long followingId) {
        return userMapper.getUserById(followingId);
    }

    public List<UserInfo> getUserInfoByUserIds(Set<Long> followingIdSet) {
        return userMapper.getUserInfoByUserIds(followingIdSet);
    }


    public PageResult<UserInfo> pageListUserInfos(JSONObject params) {
        Integer no = params.getInteger("no");
        Integer size = params.getInteger("size");
        params.put("start", (no - 1) * size);
        params.put("limit", size);
        Integer total = userMapper.pageCountUserInfos(params);
        List<UserInfo> list = new ArrayList<>();
        if (total > 0) {
            list = userMapper.pageListUserInfos(params);
        }
        return new PageResult<>(total, list);
    }

    public Map<String, Object> loginForDts(User user) throws Exception {
        //先判断传入的手机号是不是为空的
        String phone = user.getPhone();
        if (StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("传入的手机号为空");
        }

        //查询数据库看看传入的手机号是不是已经注册过
        User dbUser = getUserByPhone(phone);
        if (dbUser == null) {
            throw new ConditionException("该用户不存在!");
        }

        // 然后开始解密
        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败！");
        }
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        if (!md5Password.equals(dbUser.getPassword())) {
            throw new ConditionException("密码错误！");
        }
        // 解密成功之后返回一个token给前端 进行校验 这个时候没有任何异常的出现
        Long userId = dbUser.getId();
        String accessToken = TokenUtil.generateAccessToken(userId);
        String refreshToken = TokenUtil.generateRefreshToken(userId);
        userMapper.deleteRefreshToken(refreshToken, userId);
        userMapper.addRefreshToken(refreshToken, userId, new Date());
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", accessToken);
        tokenMap.put("refreshToken", refreshToken);
        return tokenMap;
    }

    public void logout(String refreshToken, Long userId) {
        userMapper.deleteRefreshToken(refreshToken,userId);
    }

    public String refreshAccessToken(String refreshToken) throws Exception {
        //判断当前的refreshToken是否过期
        RefreshTokenDetail refreshTokenDetail = userMapper.getRefreshToken(refreshToken);
        if (refreshTokenDetail == null){
            throw new ConditionException("555","token 过期");
        }
        Long userId = refreshTokenDetail.getUserId();
        return TokenUtil.generateAccessToken(userId);


    }
}
