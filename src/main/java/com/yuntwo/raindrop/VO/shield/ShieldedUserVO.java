package com.yuntwo.raindrop.VO.shield;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

/**
 * 屏蔽用户的信息
 */
@Data
@ApiModel
public class ShieldedUserVO {

    @ApiModelProperty(value = "用户id",name = "userId")
    private String userId;

    @ApiModelProperty(value = "用户名",name = "username")
    private String username;

    @ApiModelProperty(value = "用户头像",name = "faceImage")
    private String faceImage;
}
