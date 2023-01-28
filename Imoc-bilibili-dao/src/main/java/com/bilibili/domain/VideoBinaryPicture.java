package com.bilibili.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoBinaryPicture {

    private Long id;

    private Long videoId;

    private Integer frameNo;

    private String url;

    private Long videoTimestamp;

    private Date createTime;
}
