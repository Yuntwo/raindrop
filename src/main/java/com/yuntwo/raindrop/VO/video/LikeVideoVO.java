package com.yuntwo.raindrop.VO.video;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

@Data
@ApiModel
public class LikeVideoVO {

    @ApiModelProperty(value = "视频id",name = "video_id",example="1234")
    private String videoId;

    /**
     * 视频类型：
     * 0：普通视频
     * 1：学术视频
     */
    @ApiModelProperty(value = "视频类型",name = "type")
    private Integer type;

}
