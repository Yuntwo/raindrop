package com.yuntwo.raindrop.service;

import com.yuntwo.raindrop.VO.user.AchievementVO;

import java.util.List;

/**
 * Package short-video
 *
 * @author chenliquan on 2020/11/15 20:45
 * Description：
 */
public interface AchievementStaticService {

    List<AchievementVO> getAchievement(String userId);

    void updateAchievement(String userId);
}
