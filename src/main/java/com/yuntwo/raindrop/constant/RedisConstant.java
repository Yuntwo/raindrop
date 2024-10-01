package com.yuntwo.raindrop.constant;

/**
 * Redis中一些固定的key
 * 对于Redis中的数据结构不需要额外的配置指定数据结构，只需要在用的时候指定scan的类型，如
 * redisTemplate.opsForHash().scan(RedisConstant.MAP_KEY_USER_LIKED_COUNT, ScanOptions.NONE)
 */
public interface RedisConstant {

    /**
     * redis中视频点赞数的key（hash表）
     * 注意区分Redis本身也是一个hash表结构，键表示为hashKey，其对应的value也可以是hash表结构
     */
    public String MAP_KEY_VIDEO_LIKED_COUNT = "MAP_VIDEO_LIKED_COUNT";

    /**
     * redis中用户点赞数的key（hash表）
     */
    public  String MAP_KEY_USER_LIKED_COUNT = "MAP_USER_LIKED_COUNT";

    /**
     * 版本信息(hash表)
     */
    public String PACKAGE_INFO = "PACKAGE_INFO";

    /**
     * 版本信息(hashKey)
     */
    public String PACKAGE_INFO_NUMBER = "PACKAGE_INFO_NUMBER";

    /**
     * 版本信息(hashKey)
     */
    public String PACKAGE_INFO_DESCRIPTION = "PACKAGE_INFO_DESCRIPTION";

    /**
     * 版本信息(hashKey)
     */
    public String PACKAGE_INFO_URL = "PACKAGE_INFO_URL";

    /**
     * redis中token的格式
     */
    public String TOKEN_PREFIX = "token_%s";

    /**
     * redis中userId的格式
     */
    public String USERID_PREFIX = "userId_%s";

    /**
     * redis中举报标签的的key
     */
    public String REPORT_TAG = "reportTag";

    /**
     * redis中视频标签的key
     */
    public String VIDEO_TAG = "videoTag";

    /**
     * redis中用户手机验证码的保存的格式
     * 用户注册
     */
    public String VERIFY_CODE_REGISTER_PREFIX = "verifyCode_register_%s";

    /**
     * redis中用户手机验证码的保存的格式
     * 用户登录
     */
    public String VERIFY_CODE_LOGIN_PREFIX = "verifyCode_login_%s";

    /**
     * 用户登录的过期时间
     * redis过期时间：7天
     * */
    public Integer EXPIRE_USER_LOGIN = 180;

    /**
     * 用户验证码的过期时间：5分钟
     */
    public Integer EXPIRE_VERIFY_CODE = 300;

    /**
     * redis中视频点赞提醒key(hash)
     */
    public String REMIND_VIDEO_LIKE = "REMIND_VIDEO_LIKE";

    /**
     * redis中视频评论提醒key(hash)
     */
    public String REMIND_VIDEO_COMMENT = "REMIND_VIDEO_COMMENT";

    /**
     * redis中评论点赞提醒key(hash)
     */
    public String REMIND_COMMENT_LIKE = "REMIND_COMMENT_LIKE";
}
