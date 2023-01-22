package com.bilibili.dao;

import com.bilibili.domain.User;
import com.bilibili.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Chen Peiyu
 * @version 1.0
 * @description dao层的内容
 * @date 1/20/2023 8:23 PM
 */

@Mapper
public interface UserDaoMapper {
    User getUserByPhone(String phone);

    Integer  addUser(User user);

    Integer addUserInfo(UserInfo userInfo);


    User getUserById(Long currentUserId);

    UserInfo getUserInfoById(Long currentUserId);

    Integer updateUsers(User user);

    Integer updateUserInfos(UserInfo userInfo);
}
