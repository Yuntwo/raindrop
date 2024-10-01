package com.yuntwo.raindrop.controller.user;

import com.yuntwo.raindrop.VO.ResultVO;
import com.yuntwo.raindrop.VO.user.LoginWithCodeVO;
import com.yuntwo.raindrop.VO.user.LoginWithPwdVO;
import com.yuntwo.raindrop.VO.user.ForgetPwdVO;

import com.yuntwo.raindrop.constant.RedisConstant;

import com.yuntwo.raindrop.entity.Users;

import com.yuntwo.raindrop.enums.ResultEnum;

import com.yuntwo.raindrop.service.UserService;

import com.yuntwo.raindrop.util.PasswordUtil;
import com.yuntwo.raindrop.util.ResultVOUtil;
import com.yuntwo.raindrop.util.RedisUtil;
import com.yuntwo.raindrop.util.aliyun.SmsSUtil;

import io.swagger.annotations.*;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;

import java.util.HashMap;

/**
 * 用户不同登录方式和行为
 */
@Api(value = "用户登录接口",tags = {"用户登录"})
@RestController
@RequestMapping("/user/login")
@Slf4j
@Validated
public class LoginController {

    @Autowired
    private SmsSUtil smssUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 用户使用密码进行登录
     * @param loginWithPwdVO
     * @return
     */
    @ApiOperation(value = "用户通过密码登录",notes = "/user/login/password")
    @PostMapping("/password")
    public ResultVO loginWithPwd(@Validated @RequestBody LoginWithPwdVO loginWithPwdVO) {

        // 1. 校验参数的完整性
        if(StringUtils.isEmpty(loginWithPwdVO.getPhone()) || StringUtils.isEmpty(loginWithPwdVO.getPassword())) {
            log.error("【用户密码登录】输入参数不正确，loginWithPwdVO: {}",loginWithPwdVO.getPhone(),loginWithPwdVO.getPassword());
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }
        //2. 判断参数的正确性
        // TODO: 2020/10/4 : 前端密码解码
        String decodePassword = loginWithPwdVO.getPassword();

        Users user = userService.queryUserInfoByPhone(loginWithPwdVO.getPhone());
        if(user == null) {
            log.error("【用户密码登录】用户未注册，phone: {}",loginWithPwdVO.getPhone());
            return ResultVOUtil.error(ResultEnum.USER_IS_NOT_REGISTER);
        }

        if (! PasswordUtil.matches(user,decodePassword)) {
            log.error("【用户密码登录】用户密码输入错误，password: {}",loginWithPwdVO.getPassword());
            return ResultVOUtil.error(ResultEnum.PASSWORD_IS_WRONG.getCode(),ResultEnum.PASSWORD_IS_WRONG.getMessage());
        }

        HashMap<String, String> map = redisUtil.userLoginRedis(user.getUserId());

        // 3. 返回应答
        return ResultVOUtil.success(map);
    }

    @ApiOperation(value = "用户通过验证码登录",notes = "/user/login/verifyCode")
    @PostMapping("/verifyCode")
    public ResultVO loginWithCode(@Validated @RequestBody LoginWithCodeVO loginWithCodeVO) {
        // 1. 确认前端传递参数的完整性
        if(StringUtils.isEmpty(loginWithCodeVO.getPhone()) || StringUtils.isEmpty(loginWithCodeVO.getVerifyCode())) {
            log.error("【用户验证码登录】传入的参数不正确，loginWithCodeVO: {}",loginWithCodeVO);
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }

        // 2. 判断参数的正确性
        String phone = loginWithCodeVO.getPhone();
        // 2.1 用户是否存在
        Users user = userService.queryUserInfoByPhone(phone);
        if(user == null) {
            log.error("【用户验证码登录】用户未注册，phone: {}",phone);
            return ResultVOUtil.error(ResultEnum.USER_IS_NOT_REGISTER);
        }
        // 2.2 验证码是否正确
        // 2.2.1 验证码是否过期
        String redisVerifyCode = stringRedisTemplate.opsForValue().get(String.format(RedisConstant.VERIFY_CODE_LOGIN_PREFIX, phone));
        if(StringUtils.isEmpty(redisVerifyCode)) {
            log.error("【用户验证码登录】验证码已过期，redisVerifyCode: {}",redisVerifyCode);
            return ResultVOUtil.error(ResultEnum.VERIFY_CODE_HAS_EXPIRED.getCode(),ResultEnum.VERIFY_CODE_HAS_EXPIRED.getMessage());
        }

        // 2.2.2 验证码是否匹配
        if(!loginWithCodeVO.getVerifyCode().equals(redisVerifyCode)) {
            log.error("【用户验证码登录】验证码输入错误，redisVerifyCode: {}",redisVerifyCode);
            return ResultVOUtil.error(ResultEnum.VERIFY_CODE_IS_ERROR.getCode(),ResultEnum.VERIFY_CODE_IS_ERROR.getMessage());
        }

        HashMap<String, String> map = redisUtil.userLoginRedis(user.getUserId());

        return ResultVOUtil.success(map);
    }

    @ApiOperation(value = "获取验证码",notes = "/user/login/getVerifyCode")
    @GetMapping("/getVerifyCode/{phone}")
    public ResultVO getIdentityCode(@Size(max = 16 ) @PathVariable String phone) {
        if(StringUtils.isEmpty(phone)) {
            log.error("【用户登录-获取验证码】手机号为空，phone: {}",phone);
            return ResultVOUtil.error(ResultEnum.PHONE_IS_EMPTY.getCode(),ResultEnum.PHONE_IS_EMPTY.getMessage());
        }
        smssUtil.sendSms(phone,RedisConstant.VERIFY_CODE_LOGIN_PREFIX);

        return ResultVOUtil.successMsg(ResultEnum.SENDING_VERIFY_CODE.getMessage());
    }

    @ApiOperation(value = "用户忘记密码",notes = "/user/login/forgetPwd")
    @PostMapping("/forgetPwd")
    public ResultVO getIdentityCode(@Validated @RequestBody ForgetPwdVO forgetPwdVO) {
        // 1. 确认前端传递参数的完整性
        if(forgetPwdVO.getPhone().isEmpty() || forgetPwdVO.getNewPassword().isEmpty() ||
            forgetPwdVO.getVerifyCode().isEmpty()) {
            log.error("【用户忘记密码】传入的参数不正确，forgetPwdVO: {}",forgetPwdVO);
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }

        String phone = forgetPwdVO.getPhone();

        // 2. 判断参数的正确性

        // 2.2 手机号是否被注册
        Users user = userService.queryUserInfoByPhone(phone);
        if(user == null) {
            log.error("【用户忘记密码】用户未注册，phone: {}",forgetPwdVO.getPhone());
            return ResultVOUtil.error(ResultEnum.USER_IS_NOT_REGISTER);

        }
        // 2.3 验证码是否正确
        // 2.3.1 验证码是否过期
        String redisVerifyCode = stringRedisTemplate.opsForValue().get(String.format(RedisConstant.VERIFY_CODE_LOGIN_PREFIX, phone));
        if(StringUtils.isEmpty(redisVerifyCode)) {
            log.error("【用户忘记密码】验证码已过期，redisVerifyCode: {}",redisVerifyCode);
            return ResultVOUtil.error(ResultEnum.VERIFY_CODE_HAS_EXPIRED.getCode(),ResultEnum.VERIFY_CODE_HAS_EXPIRED.getMessage());
        }
        // 2.3.2 验证码是否匹配
        if(!forgetPwdVO.getVerifyCode().equals(redisVerifyCode)) {
            log.error("【用户忘记密码】验证码输入错误，redisVerifyCode: {}",redisVerifyCode);
            return ResultVOUtil.error(ResultEnum.VERIFY_CODE_IS_ERROR.getCode(),ResultEnum.VERIFY_CODE_IS_ERROR.getMessage());
        }

        // 3. 更新用户信息
        // 3.1 todo: 前端传过来的密码进行解密
        String decodeNewPassword = forgetPwdVO.getNewPassword();
        // 3.2 对密码进行加密
        String salt = user.getSalt();
        String encodePassword = PasswordUtil.encryptPassword(decodeNewPassword, salt);
        user.setPassword(encodePassword);

        userService.updateUserInfo(user);

        // 4. 返回应答
        return ResultVOUtil.success();

    }



}
