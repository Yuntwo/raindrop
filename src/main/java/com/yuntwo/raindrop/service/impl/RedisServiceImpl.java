package com.yuntwo.raindrop.service.impl;

import com.yuntwo.raindrop.constant.RedisConstant;

import com.yuntwo.raindrop.dto.UserLikedCountDto;
import com.yuntwo.raindrop.dto.VideoLikedCountDto;

import com.yuntwo.raindrop.enums.VideoTypeEnum;

import com.yuntwo.raindrop.mapper.UsersMapper;
import com.yuntwo.raindrop.mapper.VideosMapper;

import com.yuntwo.raindrop.service.LikeService;
import com.yuntwo.raindrop.service.RedisService;

import com.yuntwo.raindrop.util.RedisUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RedisServiceImpl implements RedisService {

//    //保存用户点赞数据的key
//    public static final String MAP_KEY_USER_LIKED = "MAP_USER_LIKED";
    //保存用户被点赞数量的key

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private VideosMapper videosMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private LikeService likeService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;


//    @Override
//    public void incrementVideoLikedCount(String hashKey) {
//        if (!redisTemplate.opsForHash().hasKey(RedisConstant.MAP_KEY_VIDEO_LIKED_COUNT, hashKey)) {
//            Long value = videosMapper.queryLikeNumByVideoId(hashKey);
//            redisTemplate.opsForHash().put(RedisConstant.MAP_KEY_VIDEO_LIKED_COUNT, hashKey, value);
//        }
//        redisTemplate.opsForHash().increment(RedisConstant.MAP_KEY_VIDEO_LIKED_COUNT, hashKey, 1);
//    }
//
//    @Override
//    public void incrementUserLikedCount(String hashKey) {
//        if (!redisTemplate.opsForHash().hasKey(RedisConstant.MAP_KEY_USER_LIKED_COUNT, hashKey)) {
//            Integer value = usersMapper.queryLikeNumByUserId(hashKey);
//            redisTemplate.opsForHash().put(RedisConstant.MAP_KEY_USER_LIKED_COUNT, hashKey, value);
//        }
//        redisTemplate.opsForHash().increment(RedisConstant.MAP_KEY_USER_LIKED_COUNT, hashKey, 1);
//    }
//
//    @Override
//    public void decrementVideoLikedCount(String hashKey) {
//        if (!redisTemplate.opsForHash().hasKey(RedisConstant.MAP_KEY_VIDEO_LIKED_COUNT, hashKey)) {
//            Long value = videosMapper.queryLikeNumByVideoId(hashKey);
//            redisTemplate.opsForHash().put(RedisConstant.MAP_KEY_VIDEO_LIKED_COUNT, hashKey, value);
//        }
//        redisTemplate.opsForHash().increment(RedisConstant.MAP_KEY_VIDEO_LIKED_COUNT, hashKey, -1);
//    }
//
//    @Override
//    public void decrementUserLikedCount(String hashKey) {
//        if (!redisTemplate.opsForHash().hasKey(RedisConstant.MAP_KEY_USER_LIKED_COUNT, hashKey)) {
//            Integer value = usersMapper.queryLikeNumByUserId(hashKey);
//            redisTemplate.opsForHash().put(RedisConstant.MAP_KEY_USER_LIKED_COUNT, hashKey, value);
//        }
//        redisTemplate.opsForHash().increment(RedisConstant.MAP_KEY_USER_LIKED_COUNT, hashKey, -1);
//    }

    @Override
    public void changeVideoLikedCount(String key, String hashKey, Integer value) {
        if (!redisTemplate.opsForHash().hasKey(key, hashKey)) {
            Integer count = usersMapper.queryLikeNumByUserId(hashKey);
            redisTemplate.opsForHash().put(key, hashKey, count);
        }
        long preCount = likeService.queryLikeNumByVideoId(hashKey, VideoTypeEnum.COMMON_VIDEO.getCode());
        preCount = preCount + value;
        redisTemplate.opsForHash().put(key,hashKey, preCount);
    }

    @Override
    public void changeUserLikedCount(String key, String hashKey, Integer value) {
        if (!redisTemplate.opsForHash().hasKey(key, hashKey)) {
            Integer count = usersMapper.queryLikeNumByUserId(hashKey);
            redisTemplate.opsForHash().put(key, hashKey, count);
        }

        long preCount = likeService.queryLikeNumByUserId(hashKey);
        preCount = preCount + value;
        redisTemplate.opsForHash().put(key,hashKey, preCount);
    }

    @Override
    public List<VideoLikedCountDto> getLikedCountFromRedis() {
        try {
            Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisConstant.MAP_KEY_VIDEO_LIKED_COUNT, ScanOptions.NONE);
            List<VideoLikedCountDto> list = new ArrayList<>();

            while (cursor.hasNext()) {
                Map.Entry<Object, Object> map = cursor.next();
                //将点赞数量存储在 LikedCountDT
                String key = (String) map.getKey();

                VideoLikedCountDto dto = new VideoLikedCountDto(key, Long.valueOf(String.valueOf(map.getValue())));
                list.add(dto);
            }
            cursor.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<UserLikedCountDto> getUserLikedCountFromRedis() {
        try {
            Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisConstant.MAP_KEY_USER_LIKED_COUNT, ScanOptions.NONE);
            List<UserLikedCountDto> list = new ArrayList<>();

            while (cursor.hasNext()) {
                Map.Entry<Object, Object> map = cursor.next();
                //将点赞数量存储在 LikedCountDT
                String key = (String) map.getKey();

                UserLikedCountDto dto = new UserLikedCountDto(key, (Integer) map.getValue());
                list.add(dto);
            }
            cursor.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean hashKeyExist(String key, String hashKey){
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public boolean setHash(String key, String hashKey, Long value){
        try{
            redisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean setHash(String key, String hashKey, Integer value){
        try{
            redisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean setHash(String key, String hashKey, String value) {
        try{
            redisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getHash(String key, String hashKey) {
        return (String) redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public boolean keyExist(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
