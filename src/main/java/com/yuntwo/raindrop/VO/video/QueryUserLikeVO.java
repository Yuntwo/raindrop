package com.yuntwo.raindrop.VO.video;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yuntwo
 */
@Data
@ApiModel
public class QueryUserLikeVO {

    @ApiModelProperty(value = "视频id",name = "video_id",example="1234")
    private String videoId;

}
