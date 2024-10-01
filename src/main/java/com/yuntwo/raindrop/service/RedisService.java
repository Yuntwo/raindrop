package com.yuntwo.raindrop.service;

import com.yuntwo.raindrop.dto.UserLikedCountDto;
import com.yuntwo.raindrop.dto.VideoLikedCountDto;

import java.util.List;

public interface RedisService {

    /**
     * redis hash表中视频点赞数加1或减1
     * @param key key
     * @param hashKey hashKey
     * @param value value
     */
    void changeVideoLikedCount(String key, String hashKey, Integer value);

    /**
     * redis hash表中用户点赞数加1或减1
     * @param key key
     * @param hashKey hashKey
     * @param value value
     */
    void changeUserLikedCount(String key, String hashKey, Integer value);

    /**
     * redis中视频点赞数同步到数据库
     * @return List<VideoLikedCountDto>
     */
    List<VideoLikedCountDto> getLikedCountFromRedis();

    /**
     * redis中用户点赞数同步到数据库
     * @return List<UserLikedCountDto>
     */
    List<UserLikedCountDto> getUserLikedCountFromRedis();

    /**
     * hash表中hashKey是否存在
     * @param hashKey hashKey
     * @param key key
     * @return boolean
     */
    boolean hashKeyExist(String key, String hashKey);

    /**
     * 设置hash表
     * @param key key
     * @param hashKey hashKey
     * @param value value
     * @return boolean
     */
    boolean setHash(String key, String hashKey, Long value);

    /**
     * 设置hash表
     * @param key key
     * @param hashKey hashKey
     * @param value value
     * @return boolean
     */
    boolean setHash(String key, String hashKey, Integer value);

    /**
     * 设置hash表
     * @param key
     * @param hashKey
     * @param value
     * @return
     */
    boolean setHash(String key, String hashKey, String value);

    /**
     * 从hash中获取信息
     * @param key key
     * @param hashKey hashKey
     * @return String
     */
    String getHash(String key, String hashKey);

    /**
     * 判断redis中key是否存在
     * @param key
     * @return
     */
    boolean keyExist(String key);
}
