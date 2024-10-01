package com.yuntwo.raindrop.mapper;

import com.yuntwo.raindrop.VO.FollowingFollowerVO;

import com.yuntwo.raindrop.entity.FollowingFollower;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户关注相关查询
 */
@Mapper
public interface UsersAndFollowingFollowerMapper extends MyMapper<FollowingFollower> {


    /**
     * 通过userId查询粉丝的信息
     * @param userId
     * @return
     */
    List<FollowingFollowerVO> queryFollowerInfoByUserId(String userId);


    /**
     * 通过userId查询关注的人信息
     * @param userId
     * @return
     */
    List<FollowingFollowerVO> queryFollowingInfoByUserId(String userId);
}
