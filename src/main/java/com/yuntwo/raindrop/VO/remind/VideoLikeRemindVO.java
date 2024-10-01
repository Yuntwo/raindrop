package com.yuntwo.raindrop.VO.remind;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class VideoLikeRemindVO {

    /**
     * 消息类型：
     * 1、评论点赞
     * 2、视频评论
     * 3、视频点赞
     */
    @ApiModelProperty(value = "类型",name = "type")
    private Integer type;

    /**
     * 被点赞的视频id
     */
    @ApiModelProperty(value = "视频id",name = "videoId")
    private String videoId;

    /**
     * 被点赞的视频title
     */
    @ApiModelProperty(value = "视频title",name = "title")
    private String title;

    /**
     * 执行评论/点赞操作的用户名
     */
    @ApiModelProperty(value = "用户名",name = "username")
    private String username;

    /**
     * 执行评论/点赞操作的用户id
     */
    @ApiModelProperty(value = "用户id",name = "userId")
    private String userId;

    @ApiModelProperty(value = "创建时间",name = "createTime")
    private Date createTime;
}
