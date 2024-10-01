package com.yuntwo.raindrop.service;

import com.yuntwo.raindrop.VO.PagedResultVO;
import com.yuntwo.raindrop.VO.VideoWithUserVO;
import com.yuntwo.raindrop.entity.Videos;

import java.util.List;

public interface VideoService {

    /**
     * 查询用户点赞的视频
     * @param page
     * @param size
     * @param userId
     * @return
     */
    PagedResultVO queryUserLikeVideoInfoByUserId(Integer page, Integer size, String userId);

    /**
     * 根据userId查询用户发布的视频列表
     * @param userId
     * @return
     */
    List<VideoWithUserVO> queryUserPublishVideoInfoByUserId(String userId);

    /**
     * 获取10大最火（点赞数最高）视频
     * @return
     */
    List<VideoWithUserVO> queryHottest10Video();

    /**
     * 保存新视频
     * @param video
     */
    void createVideo(Videos video);

    /**
     * 根据videoId查找视频
     * @param videoId
     * @return
     */
    Videos findVideoInfoByVideoId(String videoId);

    /**
     * 更新视频信息
     * @param video
     */
    void updateVideo(Videos video);

    /**
     * 根据videoId查询视频信息
     * @param videoId
     * @return
     */
    VideoWithUserVO queryVideoByVideoId(String videoId);

    /**
     * 未登录用户视频推荐
     * @return
     */
    PagedResultVO queryRecommendVideos(Integer page, Integer size);

    /**
     * 登录用户视频推荐
     * @return
     */
    PagedResultVO queryRecommendVideos(Integer page, Integer size, String userId);

    /**
     * 通过标签查询视频
     * @param tag
     * @return
     */
    PagedResultVO queryVideosByTag(String tag,Integer page, Integer size);

    /**
     * 查看用户关注的视频
     * @param userId
     * @return
     */
    PagedResultVO queryUserFollowVideosByUserId(String userId,Integer page, Integer size);

    /**
     * 查看搜索的视频
     * @param searchContent
     * @return
     */
    PagedResultVO searchVideosByTitleOrDesc(String searchContent,Integer page, Integer size);

    /**
     * 查询用户是否喜欢视频
     * @param userId
     * @param videoId
     * @return
     */
    boolean isUserLikeVideo(String userId,String videoId);

    /**
     * 获取点赞数最多的50标签为“短视频大赛的视频”
     */
    List<VideoWithUserVO> mostLikeNum();

    /**
     * 计算视频得分，在定时任务中执行
     */
    void computeScoreForVideos();

}
