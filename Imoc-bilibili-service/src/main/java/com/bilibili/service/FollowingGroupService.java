package com.bilibili.service;

import com.bilibili.dao.FollowingGroupMapper;
import com.bilibili.domain.FollowingGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 29844
 * @description 针对表【t_following_group(用户关注分组表)】的数据库操作Service
 * @createDate 2023-01-22 15:17:51
 */

@Service
public class FollowingGroupService {

    @Autowired
    private FollowingGroupMapper followingGroupMapper;

    public FollowingGroup getById(Long id) {
        return followingGroupMapper.getById(id);
    }

    public FollowingGroup getByType(String Type) {
        return followingGroupMapper.getByType(Type);
    }

    public List<FollowingGroup> getByUserId(Long userId) {
        return followingGroupMapper.getByUserId(userId);
    }

    public Integer addFollowingGroup(FollowingGroup followingGroup) {
        return followingGroupMapper.addFollowingGroup(followingGroup);
    }

    public List<FollowingGroup> getUserFollowingGroups(Long userId) {
        return followingGroupMapper.getUserFollowingGroups(userId);
    }
}
