package com.yuntwo.raindrop.entity;

import com.yuntwo.raindrop.enums.CommentStatusEnum;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Data
public class Comments {
    @Id
    @Column(name = "comment_id")
    private String commentId;

    /**
     * 视频id
     */
    @Column(name = "video_id")
    private String videoId;

    /**
     * 父评论id
     */
    @Column(name = "father_id")
    private String fatherId;

    /**
     * 留言者，评论的用户id
     */
    @Column(name = "from_user_id")
    private String fromUserId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 收到的赞数量
     */
    @Column(name = "like_num")
    private Integer likeNum;

    /**
     * 评论状态：0、发布成功 1、禁止显示(管理员操作)
     */
    private Integer status = CommentStatusEnum.PUBLIC.getCode();

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;
}
