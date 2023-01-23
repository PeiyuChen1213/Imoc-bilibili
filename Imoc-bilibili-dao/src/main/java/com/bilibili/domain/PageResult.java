package com.bilibili.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Chen Peiyu
 * @version 1.0
 * @description 分页返回的结果
 * @date 1/22/2023 10:40 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    /*总记录数*/
    private Integer total;

    /*分页数据*/
    private List<T> list;

}
