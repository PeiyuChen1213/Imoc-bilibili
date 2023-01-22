package com.bilibili.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户关注分组表
 * @TableName t_following_group
 */
@Data
public class FollowingGroup implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 关注分组名称
     */
    private String name;

    /**
     * 关注分组类型：0特别关注  1悄悄关注 2默认分组  3用户自定义分组
     */
    private String type;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    private static final long serialVersionUID = 1L;
}