package com.yuntwo.raindrop.VO.shield;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;


/**
 * 屏蔽用户操作
 */
@Data
@ApiModel
public class ShieldVO {

    @ApiModelProperty(value = "执行屏蔽操作的用户id",name = "fromUserId")
    private String fromUserId;

    @ApiModelProperty(value = "被屏蔽的用户id",name = "toUserId")
    private String toUserId;
}
