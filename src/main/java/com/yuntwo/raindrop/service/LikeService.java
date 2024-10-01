package com.yuntwo.raindrop.service;

import com.yuntwo.raindrop.VO.LikeCommentVO;
import com.yuntwo.raindrop.VO.video.LikeVideoVO;

import javax.websocket.EncodeException;
import java.io.IOException;


public interface LikeService {

    /**
     * 添加喜爱视频信息（插入MySQL点赞表）
     * @param videoId
     * @param userId likeVideoVO
     */
    void likeVideo(String videoId, String userId) throws IOException, EncodeException;

    /**
     * 添加喜爱视频信息（增加Redis点赞数）
     * @param likeVideoVO likeVideoVO
     * @param userId userId
     */
    void likeVideoNum(LikeVideoVO likeVideoVO, String userId);

    /**
     * 点赞消息推送
     * @param likeVideoVO
     * @param userId
     */
    void likeVideoRemind(LikeVideoVO likeVideoVO, String userId) throws IOException, EncodeException;


    /**
     * 添加取消喜爱视频信息
     * @param videoId
     * @param userId userId
     */
    void unlikeVideo(String videoId, String userId);

    /**
     * 添加取消喜爱视频信息（减少点赞数）
     * @param likeVideoVO likeVideoVO
     * @param userId userId
     */
    void unlikeVideoNum(LikeVideoVO likeVideoVO, String userId);

    /**
     * 添加喜爱评论信息（插入点赞表并且增加点赞数）
     * @param likeVO likeVO
     * @param userId userId
     */
    void likeComment(LikeCommentVO likeVO, String userId) throws IOException, EncodeException;

    /**
     * 添加取消喜爱评论信息（插入点赞表并且减少点赞数）
     * @param likeVO likeVO
     * @param userId userId
     */
    void unlikeComment(LikeCommentVO likeVO, String userId);

    /**
     * 判断视频点赞记录是否存在
     * @return boolean
     * @param videoId videoId
     * @param userId userId
     */
    boolean queryVideoLikeIsExists(String videoId, String userId);

    /**
     * 判断评论点赞记录是否存在
     * @return boolean
     * @param commentId commentId
     * @param userId userId
     */
    boolean queryCommentLikeIsExists(String commentId, String userId);

    /**
     * 将Redis中的点赞数量数据存入数据库
     */
    void transLikedCountFromRedis2DB();

    /**
     * 查询视频的点赞数
     * @return Long
     * @param videoId videoId
     * @param videoType videoType
     */
    Long queryLikeNumByVideoId(String videoId, Integer videoType);

    /**
     * 查询用户的点赞数
     * @param userId userId
     * @return Integer
     */
    Integer queryLikeNumByUserId(String userId);

    /**
     * 从数据库查询视频点赞数
     * @param videoId
     * @return
     */
    Long queryLikeNumFromDB(String videoId, Integer videoType);

}
