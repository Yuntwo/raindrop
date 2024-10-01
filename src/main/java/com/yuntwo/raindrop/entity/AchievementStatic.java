package com.yuntwo.raindrop.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

import java.util.Date;

/**
 * 静态的成就信息，与动态的成就信息相对
 */

@Data
public class AchievementStatic {
    @Id
    @Column(name = "achievement_id")
    private String achievementId;

    /**
     * 成就标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 成就描述
     */
    @Column(name = "desc")
    private String desc;

    /**
     * 成就种类
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 成就达成的条件
     */
    @Column(name = "condition")
    private Integer condition;

    /**
     * 达成成就的分数
     */
    @Column(name = "score")
    private Integer score;

    /**
     * 成绩等级
     */
    @Column(name = "level")
    private Integer level;

    /**
     * 成就勋章
     */
    @Column(name = "medal")
    private String medal;

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
