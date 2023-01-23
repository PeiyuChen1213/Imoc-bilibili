package com.bilibili.dao;

import com.bilibili.domain.UserMoment;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 29844
* @description 针对表【t_user_moments(用户动态表)】的数据库操作Mapper
* @createDate 2023-01-23 16:31:42
* @Entity generator.domain.UserMoment
*/
@Mapper
public interface UserMomentMapper {

    Integer addUserMoments(UserMoment userMoment);

}




