package com.bilibili.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoCoin {

    private Long id;

    private Long videoId;

    private Long userId;

    private Integer amount;

    private Date createTime;

    private Date updateTime;

}
