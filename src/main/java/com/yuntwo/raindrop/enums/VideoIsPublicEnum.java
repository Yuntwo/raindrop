package com.yuntwo.raindrop.enums;

import lombok.Getter;

/**
 * videos表中的状态信息
 */
@Getter
public enum VideoIsPublicEnum {

    PRIVATE(0,"私有"),
    PUBLIC(1,"公开"),
    ;
    private Integer code;
    private String message;

    VideoIsPublicEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
