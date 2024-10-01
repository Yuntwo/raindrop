package com.yuntwo.raindrop.rabbitmq.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 自定义消息队列处理的消息类型
 */
@Data
public class LikeVideoMessage implements Serializable {


    private static final long serialVersionUID = -2985110770814692512L;

    private String userId;

    private String videoId;

    private boolean like;
}
