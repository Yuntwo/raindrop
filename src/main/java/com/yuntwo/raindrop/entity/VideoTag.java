package com.yuntwo.raindrop.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 视频标签
 */
@Data
public class VideoTag {

    @Id
    private String id;

    /**
     * 标签内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 标签是否启用：0、当前正在使用 1、已弃用
     */
    @Column(name = "status")
    private Integer status;
}

