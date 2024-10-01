package com.yuntwo.raindrop.mapper;

import com.yuntwo.raindrop.VO.VideoWithUserVO;

import com.yuntwo.raindrop.entity.Comments;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户及其视频相关信息
 */
@Mapper
public interface UsersAndVideosMapper extends MyMapper<Comments> {

    /**
     * 根据videoId查询视频信息
     * @param videoId
     * @return
     */
    VideoWithUserVO queryVideoByVideoId(String videoId);

    /**
     * 视频推荐
     * @return
     */
    List<VideoWithUserVO> queryRecommendVideos();

    /**
     * 通过标签查询视频
     * @param tag
     * @return
     */
    List<VideoWithUserVO> queryVideosByTag(String tag);

    /**
     * 查看用户关注的视频
     * @param userId
     * @return
     */
    List<VideoWithUserVO> queryUserFollowVideosByUserId(String userId);

    /**
     * 查看搜索的视频
     * @param searchContent
     * @return
     */
    List<VideoWithUserVO> searchVideosByTitleOrDesc(@Param("searchContent") String searchContent);


    /**
     * 10大热门视频
     * @return
     */
    public List<VideoWithUserVO> queryHottest10Video();

    /**
     * 用户发布的视频
     * @param userId
     * @return
     */
    public List<VideoWithUserVO> queryUserPublishVideoInfoByUserId(String userId);

    /**
     * 用户喜欢的视频
     * @param userId
     * @return
     */
    public List<VideoWithUserVO> queryUserLikeVideoInfoByUserId(String userId);


    /**
     * 获取点赞数最高的50个标签为“短视频大赛”的视频
     */
    List<VideoWithUserVO> mostLikeNum(String tag);

}
