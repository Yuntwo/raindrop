package com.yuntwo.raindrop.mapper;

import com.yuntwo.raindrop.entity.VideoTag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Package short-video
 *
 * @author chenliquan on 2020/10/7 16:23
 * Description：
 */
@Mapper
public interface VideoTagMapper extends MyMapper<VideoTag> {

    /**
     * 查询所有视频tag
     * @return
     */
    List<String> queryAllVideoTag();
}

