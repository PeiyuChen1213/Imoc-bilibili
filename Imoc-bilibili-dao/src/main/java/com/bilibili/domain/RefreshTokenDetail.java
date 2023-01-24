package com.bilibili.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


/**
 * 刷新令牌记录表
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDetail implements Serializable {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 创建时间
     */
    private Date createTime;


}
