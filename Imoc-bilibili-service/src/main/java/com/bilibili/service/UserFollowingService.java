package com.bilibili.service;

import com.bilibili.dao.UserFollowingMapper;
import com.bilibili.domain.Constant.UserConstant;
import com.bilibili.domain.FollowingGroup;
import com.bilibili.domain.User;
import com.bilibili.domain.UserFollowing;
import com.bilibili.domain.UserInfo;
import com.bilibili.domain.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 29844
 * @description 针对表【t_user_following(用户关注表)】的数据库操作Service
 * @createDate 2023-01-22 15:14:16
 */
@Service
public class UserFollowingService {


    @Autowired
    private UserFollowingMapper userFollowingMapper;


    @Autowired
    private FollowingGroupService followingGroupService;


    @Autowired
    private UserService userService;

    /**
     * 添加用户关注数据
     */
    //添加事务的管理
    @Transactional
    public void addUserFollowings(UserFollowing userFollowing) {
        //先查找设置对应的分组
        Long groupId = userFollowing.getGroupId();
        //如果分组id没有传入，则使用默认的分组
        if (groupId == null) {
            FollowingGroup followingGroup = followingGroupService.getByType(UserConstant.USER_FOLLOWING_GROUP_TYPE_DEFAULT);
            userFollowing.setGroupId(followingGroup.getId());
        } else {
            // 如果有传入的分组id
            FollowingGroup followingGroup = followingGroupService.getById(groupId);
            if (followingGroup == null) {
                //查询不到分组
                throw new ConditionException("关注分组不存在!");
            }
        }
        //分组校验完成之后还需要校验用户 判断要关注的用户是否存在
        Long followingId = userFollowing.getFollowingId();
        //在user表中查询是否存在这个用户
        User user = userService.getUserById(followingId);
        if (user == null) {
            throw new ConditionException("关注的用户不存在!");
        }

        //校验完两个属性的存在性 开始插入到userfollowing表当中
        //首先要先根据表当中的userid和flowingId 来删除旧的数据 再插入新的数据
        Long userId = user.getId();
        Integer integer = userFollowingMapper.deleteUserFollowingByUIdAndFId(userId, followingId);
        //成功删除之后
        if (integer > 0) {
            userFollowing.setCreateTime(new Date());
            userFollowingMapper.addUserFollowing(userFollowing);
        } else {
            throw new ConditionException("删除用户关注原记录失败");
        }
    }


    /**
     * 获取用户的关注列表
     * 根据关注的id查询关注用户的基本信息
     * 根据关注的组别来进行分组
     *
     * @param userId
     * @return 根据关注分组的用户信息集合
     */
    public List<FollowingGroup> getUserFollowings(Long userId) {
        //根据用户id获取关注列表
        List<UserFollowing> list = userFollowingMapper.getUserFollowings(userId);
        // 讲关注列表的关注的id取出来成为一个集合
        Set<Long> followingIdSet = list.stream().map(UserFollowing::getFollowingId).collect(Collectors.toSet());
        List<UserInfo> userInfoList = new ArrayList<>();
        if (followingIdSet.size() > 0) {
            //根据关注列表的用户id查关注的人信息
            userInfoList = userService.getUserInfoByUserIds(followingIdSet);
        }

        // 讲查出来的关注人的信息和关注列表的信息相匹配 也就是找关注人的信息
        for (UserFollowing userFollowing : list) {
            for (UserInfo userInfo : userInfoList) {
                if (userFollowing.getFollowingId().equals(userInfo.getUserId())) {
                    userFollowing.setUserInfo(userInfo);
                }
            }
        }
        //根据用户的id查出来该id下面有几个分组
        List<FollowingGroup> groupList = followingGroupService.getByUserId(userId);

        FollowingGroup allGroup = new FollowingGroup();
        // 创建一个全部关注的分组 讲所有的关注人和对应的信息全部放到这个分组当中
        allGroup.setName(UserConstant.USER_FOLLOWING_GROUP_ALL_NAME);
        allGroup.setFollowingUserInfoList(userInfoList);
        List<FollowingGroup> result = new ArrayList<>();
        result.add(allGroup);
        for (FollowingGroup group : groupList) {
            List<UserInfo> infoList = new ArrayList<>();
            for (UserFollowing userFollowing : list) {
                // 根据组的id和用户关注表当中的组的id进行匹配
                if (group.getId().equals(userFollowing.getGroupId())) {
                    infoList.add(userFollowing.getUserInfo());
                }
            }
            // 讲该组存在的信息加入到列表当中去
            group.setFollowingUserInfoList(infoList);
            result.add(group);
        }
        return result;
    }

    /**
     * 根据用户id获取粉丝列表
     *
     * @param userId 用户id
     * @return
     */
    public List<UserFollowing> getUserFans(Long userId) {
        //为了知道粉丝的列表，必须根据followingId等于用户id
        List<UserFollowing> userFans = userFollowingMapper.getUserFans(userId);
        Set<Long> fansIdSet = userFans.stream().map(UserFollowing::getUserId).collect(Collectors.toSet());
        List<UserInfo> userInfoList = new ArrayList<>();
        if (fansIdSet.size() > 0) {
            //根据关注列表的用户id查关注的人信息
            userInfoList = userService.getUserInfoByUserIds(fansIdSet);
        }

        List<UserFollowing> followingList = userFollowingMapper.getUserFollowings(userId);
        //就是找粉丝的信息
        for (UserFollowing userFan : userFans) {
            for (UserInfo userInfo : userInfoList) {
                if (userFan.getFollowingId().equals(userInfo.getUserId())) {
                    userInfo.setFollowed(false);
                    userFan.setUserInfo(userInfo);
                }
            }
            //判断是否互粉
            for (UserFollowing following : followingList) {
                if (following.getFollowingId().equals(userFan.getUserId())) {
                    userFan.getUserInfo().setFollowed(true);
                }
            }
        }
        return userFans;
    }


    /**
     * 新增关注分组
     *
     * @param followingGroup 自定义的关注分组
     * @return
     */
    @Transactional
    public Long addUserFollowingGroups(FollowingGroup followingGroup) {
        followingGroup.setCreateTime(new Date());
        followingGroup.setType(UserConstant.USER_FOLLOWING_GROUP_TYPE_USER);
        followingGroupService.addFollowingGroup(followingGroup);
        //讲插入分组之后生成的id返回出去
        return followingGroup.getId();
    }

    /**
     * 根据用户id查询分组
     *
     * @param userId
     * @return
     */
    public List<FollowingGroup> getUserFollowingGroups(Long userId) {
        return followingGroupService.getUserFollowingGroups(userId);
    }

    /**
     * 判断当前登录的id有没有关注传入的用户信息表
     *
     * @param userInfoList 传入的用户信息表
     * @param userId       当前的登录的用户id
     * @return
     */
    public List<UserInfo> checkFollowingStatus(List<UserInfo> userInfoList, Long userId) {
        //获取当前的用户关注列表
        List<UserFollowing> userFollowingList = userFollowingMapper.getUserFollowings(userId);
        for (UserInfo userInfo : userInfoList) {
            //设置当前的关注为false
            userInfo.setFollowed(false);
            //如果发现有关注，再置为true
            for (UserFollowing userFollowing : userFollowingList) {
                if (userFollowing.getFollowingId().equals(userInfo.getUserId())) {
                    userInfo.setFollowed(true);
                }
            }
        }
        return userInfoList;
    }


}
