package com.yuntwo.raindrop.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "users")
public class Users {

    @Id
    @Column(name = "user_id")
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号、登录账号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 加密盐
     */
    private String salt;

    /**
     * 实验室id
     */
    @Column(name = "lab_id")
    private String labId;

    /**
     * 上传学术视频：0、不可以 1、可以
     */
    @Column(name = "upload_academic_video")
    private Integer uploadAcademicVideo;

    /**
     * 用户等级：0、普通用户；1、官方账号
     */
    @Column(name = "auth_level")
    private Integer authLevel;

    /**
     * 头像，如果没有，使用默认头像
     */
    @Column(name = "face_image")
    private String faceImage;

    /**
     * 用户性别： 0：男性，1：女性
     */
    private Integer sex;

    /**
     * 用户年龄
     */
    private Integer age;

    /**
     * 粉丝数量
     */
    @Column(name = "fans_num")
    private Integer fansNum;

    /**
     * 关注的人数量
     */
    @Column(name = "follow_num")
    private Integer followNum;

    /**
     * 收到的赞数量
     */
    @Column(name = "like_num")
    private Integer likeNum;


    /**
     * 个人简介
     */
    private String description;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态：0、正常使用 1、用户注销 2、违规用户
     */
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 上传视频权限：0、不能上传 1、普通视频 2、学术视频
     */
    @Column(name = "upload_authority")
    private Integer uploadAuthority;
}
