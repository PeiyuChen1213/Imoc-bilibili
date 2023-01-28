package com.bilibili.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户关注表
 *
 * @TableName t_user_following
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowing implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 关注用户id
     */
    private Long followingId;
    /**
     * 关注分组id
     */
    private Long groupId;
    /**
     * 创建时间
     */
    private Date createTime;
    private UserInfo userInfo;
}