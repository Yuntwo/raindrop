package com.yuntwo.raindrop.VO;

import lombok.Data;

import java.io.Serializable;

/**
 * 向前端返回的结果格式，比ResultEnum中多一个data
 */
@Data
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = -622823529324993068L;

    // 错误码
    private Integer code;

    // 提示信息
    private String msg;

    // 数据内容
    private T data;
}
