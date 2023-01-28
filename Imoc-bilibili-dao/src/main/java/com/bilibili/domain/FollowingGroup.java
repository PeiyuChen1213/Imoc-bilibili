package com.bilibili.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户关注分组表
 *
 * @TableName t_following_group
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowingGroup implements Serializable {
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
    /*分组的用户信息*/
    private List<UserInfo> FollowingUserInfoList;
}