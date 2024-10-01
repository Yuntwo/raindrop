package com.yuntwo.raindrop.VO.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

@ApiModel
@Data
public class UserPageVO {

    @ApiModelProperty(value = "用户id",name = "userId")
    private String userId;

    @ApiModelProperty(value = "页数",name = "page")
    private Integer page;

    @ApiModelProperty(value = "每页视频数量",name = "size")
    private Integer size;
}
