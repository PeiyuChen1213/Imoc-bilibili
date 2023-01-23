package com.bilibili.dao;

import com.bilibili.domain.UserFollowing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 29844
 * @description 针对表【t_user_following(用户关注表)】的数据库操作Mapper
 * @createDate 2023-01-22 15:14:16
 * @Entity generator.domain.UserFollowing
 */
@Mapper
public interface UserFollowingMapper {

    Integer deleteUserFollowingByUIdAndFId(@Param("userId") Long userId, @Param("followingId") Long followingId);

    Integer addUserFollowing(UserFollowing userFollowing);

    List<UserFollowing> getUserFans(Long userId);

    List<UserFollowing> getUserFollowings(Long userId);
}




