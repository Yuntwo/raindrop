package com.yuntwo.raindrop.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * 用户达成的成就
 */
@Data
public class UserAchievementStatic {
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 成就id
     */
    @Column(name = "achievement_id")
    private String achievementId;

    /**
     * 成就状态：0、正常 1、弃用
     */
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
}
