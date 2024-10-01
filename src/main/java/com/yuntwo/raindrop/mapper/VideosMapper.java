package com.yuntwo.raindrop.mapper;

import com.yuntwo.raindrop.entity.Videos;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VideosMapper extends MyMapper<Videos> {

    /**
     * 增加收到的喜爱数
     * @param videoId
     */
    void addLikeNum(String videoId);

    /**
     * 减少收到的喜爱数
     * @param videoId
     */
    void reduceLikeNum(String videoId);

    /**
     * 根据视频id返回发布者id
     * @param videoId
     * @return
     */
    String queryPublisherIdByVideoId(String videoId);

    /**
     * 增加评论数
     * @param videoId
     */
    void addCommentNum(String videoId);

    /**
     *  设置视频的点赞数
     */
    boolean setLikeNumByVideoId(String videoId, Long likeNum);

    /**
     *  根据id查询视频
     */
    Videos queryById(String videoId);

    /**
     *  根据id查询点赞数
     */
    Long queryLikeNumByVideoId(String videoId);

    /**
     * 获取所有视频
     * @return
     */
    List<Videos> queryAllVideos();

    /**
     * 根据videoId更新score
     */
    void updateScoreByVideoId(String videoId, Double score);
}
