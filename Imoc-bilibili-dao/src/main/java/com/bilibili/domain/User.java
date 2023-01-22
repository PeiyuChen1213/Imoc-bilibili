package com.bilibili.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Chen Peiyu
 * @version 1.0
 * @description User 表对应的实体类
 * @date 1/20/2023 8:14 PM
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;

    private String phone;

    private String email;

    private String password;

    private String salt;

    private Date createTime;

    private Date updateTime;

    //用户信息字段
    private UserInfo userInfo;

}
