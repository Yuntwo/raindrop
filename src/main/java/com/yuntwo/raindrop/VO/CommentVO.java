package com.yuntwo.raindrop.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
@ApiModel
public class CommentVO {

    @Size(max = 255)
    @ApiModelProperty(value = "评论内容",name = "content")
    private String content;

    @ApiModelProperty(value = "视频id",name = "videoId")
    private String videoId;

    /**
     * 视频类型：
     * 0：普通视频
     * 1：学术视频
     */
    @ApiModelProperty(value = "视频类型",name = "type")
    private Integer type;

}
