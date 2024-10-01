package com.yuntwo.raindrop.VO.remind;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class CommentLikeRemindVO {

    /**
     * 消息类型：
     * 1、评论点赞
     * 2、视频评论
     * 3、视频点赞
     */
    @ApiModelProperty(value = "类型",name = "type")
    private Integer type;

    /**
     * 评论所属的视频id
     */
    @ApiModelProperty(value = "视频id",name = "videoId")
    private String videoId;

    /**
     * 平论所属的视频title
     */
    @ApiModelProperty(value = "视频title",name = "title")
    private String title;

    /**
     * 被点赞的评论id
     */
    @ApiModelProperty(value = "评论id",name = "commentId")
    private String commentId;

    /**
     * 被点赞的评论内容(原评论内容)
     */
    @ApiModelProperty(value = "原评论内容",name = "oriCommentContext")
    private String oriCommentContext;

    /**
     * 执行评论/点赞操作的用户id
     */
    @ApiModelProperty(value = "用户id",name = "userId")
    private String userId;

    /**
     * 执行评论/点赞操作的用户名
     */
    @ApiModelProperty(value = "用户名",name = "username")
    private String username;


    @ApiModelProperty(value = "创建时间",name = "createTime")
    private Date createTime;

}
