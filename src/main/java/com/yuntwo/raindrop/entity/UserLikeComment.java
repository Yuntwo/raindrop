package com.yuntwo.raindrop.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Date;

@Data
@Table(name = "user_like_comment")
public class UserLikeComment {
    @Id
    private String id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 评论id
     */
    @Column(name = "comment_id")
    private String commentId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
}
