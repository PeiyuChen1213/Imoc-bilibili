package com.bilibili.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoLike {

    private Long id;

    private Long userId;

    private Long videoId;

    private Date createTime;
}
