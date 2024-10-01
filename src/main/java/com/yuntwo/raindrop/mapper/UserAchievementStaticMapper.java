package com.yuntwo.raindrop.mapper;

import com.yuntwo.raindrop.entity.UserAchievementStatic;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserAchievementStaticMapper extends MyMapper<UserAchievementStatic> {

    List<String> queryAchievementStaticIdByUserId(String userId);

    List<UserAchievementStatic> queryAchievementStaticByUserId(String userId);

    UserAchievementStatic queryIsExist(String userId, String achievementId);
}
