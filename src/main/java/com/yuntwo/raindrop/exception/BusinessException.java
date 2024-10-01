package com.yuntwo.raindrop.exception;

import com.yuntwo.raindrop.enums.ResultEnum;

import lombok.Getter;

/**
 * 统一的业务异常
 */

@Getter
public class BusinessException extends RuntimeException {

    private Integer code;
    private String message;

    // 表单验证中传递的一些信息
    private Object data;

    public BusinessException(ResultEnum resultEnum) {
        super();
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }

    public BusinessException(ResultEnum resultEnum, Object data) {
        super();
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
        this.data = data;
    }
}
