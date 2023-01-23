package com.bilibili.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Chen Peiyu
 * @version 1.0
 * @description UserInfo实体类
 * @date 1/20/2023 8:17 PM
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfo {

    private Long id;
    private Long userId;

    private String nick;

    private String avatar;

    private String sign;

    private String gender;

    private String birth;

    private Boolean followed;

    private Date createTime;

    private Date updateTime;


}
