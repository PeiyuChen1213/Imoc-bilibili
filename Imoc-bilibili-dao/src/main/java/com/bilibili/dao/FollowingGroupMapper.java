package com.bilibili.dao;

import com.bilibili.domain.FollowingGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 29844
* @description 针对表【t_following_group(用户关注分组表)】的数据库操作Mapper
* @createDate 2023-01-22 15:17:51
* @Entity generator.domain.FollowingGroup
*/
@Mapper
public interface FollowingGroupMapper{
   FollowingGroup getById(Long id);

   FollowingGroup getByType(String type);

    List<FollowingGroup> getByUserId(Long userId);

    Integer addFollowingGroup(FollowingGroup followingGroup);

    List<FollowingGroup> getUserFollowingGroups(Long userId);
}




