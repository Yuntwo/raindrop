package com.yuntwo.raindrop.enums;

import lombok.Getter;

/**
 * comments表中的状态信息
 */
@Getter
public enum CommentStatusEnum {

    PUBLIC(0,"公开"),
    FORBID(1,"禁止显示"),
    ;
    private Integer code;
    private String message;

    CommentStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
