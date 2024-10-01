package com.yuntwo.raindrop.mapper;

import com.yuntwo.raindrop.entity.UserLikeVideo;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserLikeVideoMapper extends MyMapper<UserLikeVideo> {

    /**
     * 判断视频点赞记录是否存在
     * @return
     */
    UserLikeVideo queryVideoLikeIsExists(String videoId, String userId);
}
