package com.yuntwo.raindrop.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "user_like_video")
public class UserLikeVideo {
    @Id
    private String id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 视频id
     */
    @Column(name = "video_id")
    private String videoId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
}
