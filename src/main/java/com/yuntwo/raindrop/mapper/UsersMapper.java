package com.yuntwo.raindrop.mapper;

import com.yuntwo.raindrop.entity.Users;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UsersMapper extends MyMapper<Users> {

    /**
     * 点赞数加1
     *
     * @param userId userId
     */
    void addLikeNum(String userId);

    /**
     * 粉丝数加1
     * @param userId userId
     */
    void addFansNum(String userId);

    /**
     * 关注的人加1
     *
     * @param userId userId
     */
    void addFollowNum(String userId);

    /**
     * 点赞数减1
     *
     * @param userId userId
     */
    void reduceLikeNum(String userId);

    /**
     * 粉丝数减1
     *
     * @param userId userId
     */
    void reduceFansNum(String userId);

    /**
     * 关注的人减1
     *
     * @param userId userId
     */
    void reduceFollowNum(String userId);


    /**
     * 通过手机号查询用户信息
     *
     * @param phone phone
     * @return Users
     */
    Users queryPhoneIsExist(String phone);

    /**
     * 查询用户名是否存在
     *
     * @param username username
     * @return Users
     */
    Users queryUsernameIsExist(String username);

    /**
     * 创建新的用户
     *
     * @param user user
     */
    void createUser(Users user);

    /**
     * 更新用户信息
     *
     * @param user user
     */
    void updateUserInfo(Users user);

    /**
     * 通过用户id查询用户
     *
     * @param userId userId
     * @return Users
     */
    Users queryUserByUserId(String userId);

    /**
     * 查询点赞数
     *
     * @param userId userId
     * @return Integer
     */
    Integer queryLikeNumByUserId(String userId);

    /**
     * 设置用户获赞数
     *
     * @param userId userId
     * @param likeNum likeNum
     * @return boolean
     */
    boolean setLikeNumByUserId(String userId,Integer likeNum);

    /**
     * 查询用户状态
     *
     * @param userId userId
     * @return Integer
     */
    Integer queryStatus(String userId);

    /**
     * 查询用户视频上传权限
     * @param userId
     * @return
     */
    Integer queryUploadAuthority(String userId);
}
