package com.bilibili.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthRole {

    private Long id;

    private String name;

    private String code;

    private Date createTime;

    private Date updateTime;

}
