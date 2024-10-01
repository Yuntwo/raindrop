package com.yuntwo.raindrop.VO;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

@Data
@ApiModel
public class LikeCommentVO {

    @ApiModelProperty(value = "评论id", name = "comment_id", example = "1234")
    private String commentId;

    /**
     * 视频类型：
     * 0：普通视频
     * 1：学术视频
     */
    @ApiModelProperty(value = "视频类型",name = "type")
    private Integer type;

}
