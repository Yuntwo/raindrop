package com.yuntwo.raindrop.mapper;

import com.yuntwo.raindrop.entity.AchievementStatic;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AchievementStaticMapper extends MyMapper<AchievementStatic> {

    AchievementStatic queryAchievementStaticByAchievementId(String achievementId);

    List<Integer> queryAchievementStaticType();

    List<AchievementStatic> queryAchievementStaticByType(Integer type);
}
