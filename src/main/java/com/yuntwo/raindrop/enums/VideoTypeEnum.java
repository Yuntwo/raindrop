package com.yuntwo.raindrop.enums;

import lombok.Getter;

@Getter
public enum VideoTypeEnum {
    COMMON_VIDEO(0,"普通视频"),
    ACADEMIC_VIDEO(1,"学术视频"),
    ;


    private Integer code;
    private String message;

    VideoTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
