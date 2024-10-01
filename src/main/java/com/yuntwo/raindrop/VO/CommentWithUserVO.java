package com.yuntwo.raindrop.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

import java.util.Date;

@Data
@ApiModel
public class CommentWithUserVO {

    @ApiModelProperty(value = "评论id",name = "commentId",example="1234")
    private String commentId;

    @ApiModelProperty(value = "视频id",name = "videoId",example="1234")
    private String videoId;

    @ApiModelProperty(value = "评论者id",name = "fromUserId",example="1234")
    private String fromUserId;

    @ApiModelProperty(value = "评论者头像",name = "fromUserFaceImg",example="1234")
    private String fromUserFaceImg;

    @ApiModelProperty(value = "评论者昵称",name = "fromUserName",example="1234")
    private String fromUserName;

    @ApiModelProperty(value = "被回复者id",name = "fatherId",example="1234", required=false)
    private String fatherId;

    @ApiModelProperty(value = "被回复者昵称",name = "fatherName",example="1234", required=false)
    private String fatherName;

    @ApiModelProperty(value = "被回复者头像",name = "fatherFaceImg",example="1234")
    private String fatherFaceImg;

    @ApiModelProperty(value = "评论内容",name = "content",example="这是一条评论")
    private String content;

    @ApiModelProperty(value = "点赞数",name = "likeNum",example="101")
    private Integer likeNum;

//    @ApiModelProperty(value = "评论状态",name = "status",example="0")
//    private Integer status;

    @ApiModelProperty(value = "是否点赞",name = "likeStatus",example = "111", required = false)
    private String likeStatus;

    @ApiModelProperty(value = "评论时间",name = "createTime",example="Fri Oct 02 18:45:23 CST 2020")
    private Date createTime;
}
