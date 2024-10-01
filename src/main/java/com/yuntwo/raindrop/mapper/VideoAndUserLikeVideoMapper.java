package com.yuntwo.raindrop.mapper;

import com.yuntwo.raindrop.VO.VideoWithUserVO;

import com.yuntwo.raindrop.entity.Videos;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户给视频点赞
 */
@Mapper
public interface VideoAndUserLikeVideoMapper extends MyMapper<Videos> {

    /**
     * 用户喜欢的视频
     * @param userId
     * @return
     */
    public List<VideoWithUserVO> queryUserLikeVideoInfoByUserId(String userId);


}
