package com.yuntwo.raindrop.service;

import com.yuntwo.raindrop.VO.FollowingFollowerVO;
import com.yuntwo.raindrop.entity.Users;

import java.util.List;

/**
 * 用户的相关服务
 */
public interface UserService {

    /**
     * 查询手机号是否存在
     * @param phone：用户手机号
     * @return: 存在return true
     */
    boolean queryPhoneIsExist(String phone);


    /**
     * 查询用户名是否存在
     * @param username
     * @return
     */
    boolean queryUsernameIsExist(String username);

    /**
     * 根据手机号查询用户信息
     * @param phone
     * @return
     */
    Users queryUserInfoByPhone(String phone);

    /**
     * 新建用户
     * @param user
     */
    void createUser(Users user);

    /**
     * 更新用户信息
     * @param user
     */
    void updateUserInfo(Users user);


    /**
     * 根据userId查询用户是否存在，用户注销、被禁则认为不存在
     * @param userId
     * @return
     */
    boolean queryUserIsExistByUserId(String userId);


    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    Users queryUserInfoByUserId(String userId);

    /**
     * 根据userId查询用户粉丝列表
     * @param userId
     * @return
     */
    List<FollowingFollowerVO> queryFollowerInfoByUserId(String userId);

    /**
     * 根据userId查询用户关注的人的列表
     * @param userId
     * @return
     */
    List<FollowingFollowerVO> queryFollowingInfoByUserId(String userId);

    /**
     * 关注别的用户
     * @param followerId
     * @param followingId
     * @return
     */
    void followOthers(String followerId, String followingId);

    /**
     * 取消关注
     * @param followerId
     * @param followingId
     */
    void cancelFollowOthers(String followerId, String followingId);

    /**
     * 判断是否存在粉丝关系
     * @param followerId
     * @param followingId
     * @return
     */
    boolean isFollowRelationExist(String followerId, String followingId);

    /**
     * 查询用户是否合法
     * @param userId
     * @return
     */
    boolean isLegal(String userId);


    /**
     * 查询用户是否可以上传学术视频
     * @param userId
     * @return
     */
    boolean canUploadAcademicVideo(String userId);
}
