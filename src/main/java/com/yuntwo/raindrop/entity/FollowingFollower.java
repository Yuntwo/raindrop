package com.yuntwo.raindrop.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "following_follower")
public class FollowingFollower {
    @Id
    private String id;

    /**
     * 被关注者id
     */
    @Column(name = "following_id")
    private String followingId;

    /**
     * 关注者id
     */
    @Column(name = "follower_id")
    private String followerId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
}
