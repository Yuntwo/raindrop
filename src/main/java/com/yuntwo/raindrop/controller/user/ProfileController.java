package com.yuntwo.raindrop.controller.user;

import com.yuntwo.raindrop.annotation.UserAuthentication;

import com.yuntwo.raindrop.VO.*;
import com.yuntwo.raindrop.VO.user.*;
import com.yuntwo.raindrop.constant.VideoConstant;

import com.yuntwo.raindrop.entity.Users;

import com.yuntwo.raindrop.enums.AuthAopEnum;
import com.yuntwo.raindrop.enums.ResultEnum;
import com.yuntwo.raindrop.enums.VideoTypeEnum;

import com.yuntwo.raindrop.service.AchievementStaticService;
import com.yuntwo.raindrop.service.LikeService;
import com.yuntwo.raindrop.service.UserService;
import com.yuntwo.raindrop.service.VideoService;

import com.yuntwo.raindrop.util.FastDfsUtil;
import com.yuntwo.raindrop.util.RedisUtil;
import com.yuntwo.raindrop.util.PasswordUtil;
import com.yuntwo.raindrop.util.ResultVOUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

@Api(value = "用户个人信息接口", tags = {"用户个人信息"})
@RestController
@RequestMapping("/user")
@Slf4j
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private FastDfsUtil fastDfsUtil;

    @Autowired
    private LikeService likeService;

    @Autowired
    private AchievementStaticService achievementStaticService;

    @Value("${yuntwo.fdfsUrl}")
    private String prefixed;

    @ApiOperation(value = "获取用户信息", notes = "/user/getProfile")
    @PostMapping("/getProfile")
//    @UserAuthentication(pass = true,role = AuthAopEnum.USER)
    public ResultVO getProfile(@RequestBody UserIdVO userIdVO, HttpServletRequest request) {

        String userId;
        if (!StringUtils.isEmpty(userIdVO.getUserId())) {
            userId = userIdVO.getUserId();
        } else {
            userId = redisUtil.getUserIdByToken(request);
        }

        // 1. 判断用户是否存在
        Users user = userService.queryUserInfoByUserId(userId);
        if(user == null){
            log.error("【获得用户信息】用户不存在，userId: {}", userId);
            return ResultVOUtil.error(ResultEnum.USER_IS_NOT_EXIST.getCode(),ResultEnum.USER_IS_NOT_EXIST.getMessage());
        }

        // 隐藏部分个人信息
        user.setPassword(null);
        user.setSalt(null);
        user.setLikeNum(likeService.queryLikeNumByUserId(userId));

        // 2. 返回用户信息
        return ResultVOUtil.success(user);
    }

    @ApiOperation(value = "更新用户信息",notes = "/user/updateProfile")
    @PutMapping("/updateProfile")
    @UserAuthentication(pass = true, role = AuthAopEnum.USER)
    public ResultVO updateProfile(@Validated  @RequestBody UpdateProfileVO updateProfileVO, HttpServletRequest request) {

        // 1. 判断用户是否存在
        String userId = redisUtil.getUserIdByToken(request);
        Users user = userService.queryUserInfoByUserId(userId);

        if (user == null) {
            log.error("【修改用户信息】用户不存在，userId: {}", userId);
            return ResultVOUtil.error(ResultEnum.USER_IS_NOT_EXIST.getCode(),ResultEnum.USER_IS_NOT_EXIST.getMessage());
        }

        // 2. 写入用户信息

        // bug: 将 null 信息赋值给了user，导致更新失败
        // BeanUtils.copyProperties(userVO, user);

        // 用户名不能重复
        if(!user.getUsername().equals(updateProfileVO.getUsername())) {
            if(userService.queryUsernameIsExist(updateProfileVO.getUsername())) {
                log.error("【修改用户信息】用户名已存在，username: {}", updateProfileVO.getUsername());
                return ResultVOUtil.error(ResultEnum.USERNAME_IS_EXIST.getCode(),ResultEnum.USERNAME_IS_EXIST.getMessage());
            }
        }

        user.setUsername(updateProfileVO.getUsername());
        user.setDescription(updateProfileVO.getDescription());
        user.setEmail(updateProfileVO.getEmail());
        user.setSex(updateProfileVO.getSex());
        user.setAge(updateProfileVO.getAge());

        userService.updateUserInfo(user);

        // 隐藏部分个人信息
        user.setPassword(null);
        user.setSalt(null);

        return ResultVOUtil.success(user);
    }

    @ApiOperation(value = "改变头像", notes = "/user/uploadImage")
    @PostMapping("/uploadImage")
    @UserAuthentication(pass = true, role = AuthAopEnum.USER)
    public ResultVO uploadFace(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

        String userId = redisUtil.getUserIdByToken(request);

        if (file.isEmpty()) {
            log.error("【用户上传头像】选择文件不能为空，file: {}", file);
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }
        Users user = userService.queryUserInfoByUserId(userId);

        try {
            String path = fastDfsUtil.uploadFile(file);
            if (!StringUtils.isEmpty(path)) {
                user.setFaceImage(path);
                userService.updateUserInfo(user);
            } else {
                return ResultVOUtil.error(ResultEnum.USER_UPDATE_FACE_ERROR.getCode(),ResultEnum.USER_UPDATE_FACE_ERROR.getMessage());
            }
        } catch (Exception e) {
            log.error("【上传头像】服务异常，Exception: {}", e.getMessage());
            return ResultVOUtil.error(ResultEnum.USER_UPDATE_FACE_ERROR.getCode(),ResultEnum.USER_UPDATE_FACE_ERROR.getMessage());
        }

        // 隐藏部分个人信息
        user.setPassword(null);
        user.setSalt(null);
        // user.setFaceImage(prefixed + user.getFaceImage());

        return ResultVOUtil.success(user);
    }

    @ApiOperation(value = "更改密码", notes = "/user/changePassword")
    @PutMapping("/changePassword")
    @UserAuthentication(pass = true, role = AuthAopEnum.USER)
    public ResultVO changePassword(@RequestBody PasswordVO passwordVO,
                                   HttpServletRequest request) {

        String userId = redisUtil.getUserIdByToken(request);

        // 1. 确认前端传递参数的完s整性
        if (passwordVO.getPassword().isEmpty() || passwordVO.getNewPassword().isEmpty()) {
            log.error("【修改密码】传入的参数不正确，changePasswordVO: {}", passwordVO);
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }

        // 2. 判断参数的正确性
        // 2.1 判断用户是否存在
        Users user = userService.queryUserInfoByUserId(userId);
        if (user == null) {
            log.error("【修改密码】用户不存在，userId: {}", userId);
            return ResultVOUtil.error(ResultEnum.USER_IS_NOT_EXIST.getCode(),ResultEnum.USER_IS_NOT_EXIST.getMessage());
        }

        // 2.2 判断旧密码是否正确
        // 2.2.1 todo:前端传过来的密码进行解密
        String decodePassword = passwordVO.getPassword();

        if (!PasswordUtil.matches(user, decodePassword)) {
            log.error("【修改密码】密码输入错误，password: {},{}", decodePassword, user.getPassword());
            return ResultVOUtil.error(ResultEnum.PASSWORD_IS_WRONG.getCode(),ResultEnum.PASSWORD_IS_WRONG.getMessage());
        }

        // 3. 更新用户信息
        // 3.1 对密码进行加密
        String decodeNewPassword = passwordVO.getNewPassword();
        String salt = user.getSalt();
        String encodePassword = PasswordUtil.encryptPassword(decodeNewPassword, salt);
        user.setPassword(encodePassword);

        // 3.2 写入用户信息
        userService.updateUserInfo(user);

        // 返回应答
        return ResultVOUtil.success();
    }


    /**
     * 这里似乎也没怎么进行token校验，只是需要用到特定用户相关的接口时才使用token来判断登录用户
     * 这里很巧妙地来两种类型的用户放在了一个接口中
     * @param userIdVO 这两种参数其实都是前端HTTP请求中的的数据，只不过后台把它们处理成两种不同的数据结构
     * @param request
     * @return
     */
    @UserAuthentication(pass = true, role = AuthAopEnum.USER)
    @ApiOperation(value = "用户粉丝列表", notes = "/user/getFollower")
    @PostMapping("/getFollower")
    public ResultVO getFollower(@RequestBody UserIdVO userIdVO, HttpServletRequest request) {
        String userId;
        if (!StringUtils.isEmpty(userIdVO.getUserId())) {
            // 别的用户粉丝列表
            userId = userIdVO.getUserId();
        } else {
            //本人的粉丝列表
            userId = redisUtil.getUserIdByToken(request);
        }
        List<FollowingFollowerVO> result = userService.queryFollowerInfoByUserId(userId);
        return ResultVOUtil.success(result);

    }

    @UserAuthentication(pass = true, role = AuthAopEnum.USER)
    @ApiOperation(value = "用户关注的人列表", notes = "/user/getFollowing")
    @PostMapping("/getFollowing")
    public ResultVO getFollowing(@RequestBody UserIdVO userIdVO, HttpServletRequest request) {
        String userId;
        if (!StringUtils.isEmpty(userIdVO.getUserId())) {
            userId = userIdVO.getUserId();
        } else {
            userId = redisUtil.getUserIdByToken(request);
        }
        List<FollowingFollowerVO> result = userService.queryFollowingInfoByUserId(userId);
        return ResultVOUtil.success(result);
    }

    @UserAuthentication(pass = true, role = AuthAopEnum.USER)
    @ApiOperation(value = "用户点赞的视频信息", notes = "/user/likeVideo")
    @PostMapping("/likeVideo")
    public ResultVO likeVideo(@RequestBody UserPageVO userPageVO, HttpServletRequest request) {
        String userId;
        if (!StringUtils.isEmpty(userPageVO.getUserId())) {
            userId = userPageVO.getUserId();
        } else {
            userId = redisUtil.getUserIdByToken(request);
        }

        Integer page = StringUtils.isEmpty(userPageVO.getPage()) ? 1 : userPageVO.getPage();
        Integer size = StringUtils.isEmpty(userPageVO.getSize()) ? VideoConstant.PAGE_SIZE : userPageVO.getSize();

        PagedResultVO result = videoService.queryUserLikeVideoInfoByUserId(page, size, userId);
        return ResultVOUtil.success(result);
    }

    //    @UserAuthentication(pass = true, role = AuthAopEnum.USER)
    @ApiOperation(value = "用户发布的视频信息", notes = "/user/publishVideo")
    @PostMapping("/publishVideo")
    public ResultVO publishVideo(@RequestBody UserIdVO userIdVO, HttpServletRequest request) {
        String userId;
        if (!StringUtils.isEmpty(userIdVO.getUserId())) {
            userId = userIdVO.getUserId();
        } else {
            userId = redisUtil.getUserIdByToken(request);
        }
        List<VideoWithUserVO> result = videoService.queryUserPublishVideoInfoByUserId(userId);
        for(VideoWithUserVO v : result){
            v.setLikeNum(likeService.queryLikeNumByVideoId(v.getVideoId(), VideoTypeEnum.COMMON_VIDEO.getCode()));
        }
        return ResultVOUtil.success(result);
    }

    @UserAuthentication(pass = true, role = AuthAopEnum.USER)
    @ApiOperation(value = "用户是否被关注", notes = "/user/isFollowed")
    @PostMapping("/isFollowed")
    public ResultVO isFollowed(@RequestParam(name = "followingId") String followingId, HttpServletRequest request) {

        if (StringUtils.isEmpty(followingId)) {
            log.error("【用户是否被关注】传入的参数不正确，followingId: {}", followingId);
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }
        String followerId = redisUtil.getUserIdByToken(request);
        return ResultVOUtil.success(userService.isFollowRelationExist(followerId, followingId));
    }


    @UserAuthentication(pass = true, role = AuthAopEnum.USER)
    @ApiOperation(value = "关注用户", notes = "/user/followOthers")
    @PostMapping("/followOthers")
    public ResultVO followOthers(@RequestBody FollowingFollowerVO followingFollowerVO, HttpServletRequest request) {

        String followingId = followingFollowerVO.getUserId();
        if (StringUtils.isEmpty(followingId)) {
            log.error("【关注用户】传入的参数不正确，followingId: {}", followingId);
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }

        String followerId = redisUtil.getUserIdByToken(request);

        Users users = userService.queryUserInfoByUserId(followingId);
        if(users == null) {
            log.error("【关注用户】用户不存在，followingId: {}", followingId);
            return ResultVOUtil.error(ResultEnum.USER_IS_NOT_EXIST.getCode(),ResultEnum.USER_IS_NOT_EXIST.getMessage());
        }

        userService.followOthers(followerId, followingId);
        return ResultVOUtil.successMsg(ResultEnum.FOLLOW_SUCCESS.getMessage());
    }

    @UserAuthentication(pass = true, role = AuthAopEnum.USER)
    @ApiOperation(value = "取消关注", notes = "/user/cancelFollowOthers")
    @PostMapping("/cancelFollowOthers")
    public ResultVO cancelFollowOthers(@RequestParam(name = "followingId") String followingId, HttpServletRequest request) {
        if (StringUtils.isEmpty(followingId)) {
            log.error("【取消关注】传入的参数不正确，followingId: {}", followingId);
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }

        String followerId = redisUtil.getUserIdByToken(request);

        userService.cancelFollowOthers(followerId, followingId);
        return ResultVOUtil.successMsg(ResultEnum.CANCEL_FOLLOW_SUCCESS.getMessage());
    }

    @ApiOperation(value = "获取用户成就", notes = "/user/getAchievement")
    @PostMapping("/getAchievement")
    @UserAuthentication(pass = true,role = AuthAopEnum.USER)
    public ResultVO getAchievement(@RequestBody UserIdVO userIdVO, HttpServletRequest request) {
        String userId;
        if (!StringUtils.isEmpty(userIdVO.getUserId())) {
            userId = userIdVO.getUserId();
        } else {
            userId = redisUtil.getUserIdByToken(request);
        }

        // 1. 判断用户是否存在
        Users user = userService.queryUserInfoByUserId(userId);
        if(user == null){
            log.error("【获得用户信息】用户不存在，userId: {}", userId);
            return ResultVOUtil.error(ResultEnum.USER_IS_NOT_EXIST.getCode(),ResultEnum.USER_IS_NOT_EXIST.getMessage());
        }

        achievementStaticService.updateAchievement(userId);
        List<AchievementVO> result = achievementStaticService.getAchievement(userId);

        if(CollectionUtils.isEmpty(result)) {
            // TODO: 2020/10/12 success
            return ResultVOUtil.error(ResultEnum.ACHIEVEMENT_IS_EMPTY.getCode(),ResultEnum.ACHIEVEMENT_IS_EMPTY.getMessage());
        }

        // 2. 返回用户信息
        return ResultVOUtil.success(result);
    }

    @UserAuthentication(pass = true, role = AuthAopEnum.USER)
    @ApiOperation(value = "用户是否可以上传学术视频", notes = "/user/canUploadAcademicVideo")
    @PostMapping("/canUploadAcademicVideo")
    public ResultVO canUploadAcademicVideo(HttpServletRequest request) {

        String userId = redisUtil.getUserIdByToken(request);

        return ResultVOUtil.success(userService.canUploadAcademicVideo(userId));
    }


}
