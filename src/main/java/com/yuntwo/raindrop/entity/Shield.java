package com.yuntwo.raindrop.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

import java.util.Date;

/**
 * 用户屏蔽信息
 */
@Data
public class Shield {
    @Id
    private String id;

    /**
     * 执行屏蔽操作的id
     */
    @Column(name = "from_user_id")
    private String fromUserId;

    /**
     * 被屏蔽的用户id
     */
    @Column(name = "to_user_id")
    private String toUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
}
