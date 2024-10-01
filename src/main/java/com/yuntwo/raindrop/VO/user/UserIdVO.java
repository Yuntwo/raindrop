package com.yuntwo.raindrop.VO.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

@ApiModel
@Data
public class UserIdVO {
    @ApiModelProperty(value = "用户id",name = "userId",required=true)
    private String userId;
}
