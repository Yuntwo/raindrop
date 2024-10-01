package com.yuntwo.raindrop.enums;

import lombok.Getter;

/**
 * ResultVO中的状态码和消息取值情况
 */

@Getter
public enum ResultEnum {

    SUCCESS(200,"成功"),

    INTERNAL_SERVER_ERROR(500, "内部服务器错误"),

    // 公共错误码-系统设置-用户授权管理：用户、授权：40001起
    UNAUTHORIZED(40001,"用户权限错误"),
    USER_IS_NOT_EXIST(40002,"用户不存在"),
    USER_IS_NOT_LOGIN(40003,"用户没登录"),
    AOP_SERVER_ERROR(40004,"aop-用户权限鉴定错误"),
    ALIYUN_CALL_BACK_ERROR(40005,"阿里云视频回调错误"),
    USER_IS_NOT_REGISTER(40006,"用户未注册"),

    // 公共错误码-系统设置-基础服务：数据字段、地级等：50001起
    PARAM_ERROR(50001, "输入参数不正确"),
    VERIFY_CODE_HAS_EXPIRED(50002,"验证码已过期"),
    VERIFY_CODE_IS_ERROR(50003,"验证码输入错误"),
    USER_UPDATE_FACE_ERROR(50004,"上传用户头像错误"),
    PASSWORD_IS_WRONG(50005,"用户原密码输入错误"),
    USER_IS_REGISTERED(50006,"用户已被注册"),
    VIDEO_IS_NOT_EXIST(50007,"视频不存在"),
    ALIYUN_VIDEO_VOD_ERROR(50008,"阿里云视频点播错误"),
    PHONE_IS_EMPTY(50009,"手机号为空"),
    SEARCH_CONTENT_IS_NULL(50010,"输入的搜索内容为空"),
    REPORT_CONTENT_IS_INCOMPLETE(50011,"请输入必填的举报内容"),
    COMMENT_FAIL(50012,"评论失败"),
    UPDATE_VIDEO_COVER_ERROR(50013,"上传视频头像失败"),
    COMMENT_IS_EMPTY(50014,"当前视频还没有评论信息"),
    USERNAME_IS_EXIST(50015,"用户名已存在"),
    ACHIEVEMENT_IS_EMPTY(50016,"没有达成任何成就"),
    UNKNOWN_ERROR(50017,"未知错误"),
    PROMOTION_PICTURE_IS_EMPTY(50018, "推送图片获取失败"),
    VIDEO_IS_LIKED(50019, "视频已点赞，不能重复点赞"),
    VIDEO_IS_NOT_LIKED(50020, "视频未点赞，不能重复取消"),
    LAB_IS_EMPTY(50021, "实验室为空"),
    VIDEO_TYPE_NOT_EXIST(50022, "视频类型不存在"),
    GET_ACADEMIC_VIDEO_RECOMMEND_LIST_FAILED(50023, "获取学术视频推荐列表失败"),
    GET_PACKAGE_INFO_FAILED(50024, "获取安装包信息失败"),
    UPDATE_USER_PROFILE_FAIL(50025,"修改用户信息失败"),
    SHIELD_USER_FAIL(50026,"屏蔽用户失败"),
    GET_SHIELD_USER_LIST_FAIL(50027, "获取已屏蔽用户列表失败"),
    UPLOAD_ACADEMIC_PICTURE_FAIL(50028,"上传学术视频图片失败"),
    METHOD_ARGUMENT_VALID_ERROR(50029,"参数校验异常，字符串长度或数字大小不在范围内"),



    // 业务提示消息
    SENDING_VERIFY_CODE(10,"正在发送验证码"),
    UPLOAD_VIDEO_SUCCESS(11,"视频正在上传，请稍后"),
    USER_LOGIN_SUCCESS(12,"用户登录成功"),
    USER_LOGOUT_SUCCESS(13,"用户注销成功"),
    REPORT_SUCCESS(14,"举报成功"),
    FOLLOW_SUCCESS(15,"关注成功"),
    CANCEL_FOLLOW_SUCCESS(16,"取消关注成功"),

    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
