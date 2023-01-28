package com.bilibili.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoView {

    private Long id;

    private Long videoId;

    private Long userId;

    private String clientId;

    private String ip;

    private Date createTime;

}
