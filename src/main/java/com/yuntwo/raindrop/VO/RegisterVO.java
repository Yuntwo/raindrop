package com.yuntwo.raindrop.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * 用户注册需要的信息
 */
@Data
@ApiModel
public class RegisterVO {

    @Size(max = 16)
    @ApiModelProperty(value = "手机号",name = "phone",example="123444")
    private String phone;

    @ApiModelProperty(value = "新密码",name = "newPassword",example="newPassword")
    private String newPassword;

    @ApiModelProperty(value = "验证码",name = "verifyCode",example="345678")
    private String verifyCode;

}
