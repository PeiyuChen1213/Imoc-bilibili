package com.bilibili.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户关注表
 * @TableName t_user_following
 */
@Data
public class UserFollowing implements Serializable {
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
    private Integer followingId;

    /**
     * 关注分组id
     */
    private Integer groupId;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}