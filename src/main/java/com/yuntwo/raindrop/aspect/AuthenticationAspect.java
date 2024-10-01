package com.yuntwo.raindrop.aspect;

import com.yuntwo.raindrop.annotation.UserAuthentication;
import com.yuntwo.raindrop.constant.RedisConstant;
import com.yuntwo.raindrop.enums.AuthAopEnum;
import com.yuntwo.raindrop.enums.ResultEnum;
import com.yuntwo.raindrop.exception.UserAuthorizeException;
import com.yuntwo.raindrop.service.UserService;
import com.yuntwo.raindrop.util.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 用户鉴权切面
 */
@Aspect
@Component
@Slf4j
public class AuthenticationAspect {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 定义切点，是切面是更大的逻辑集合，一个切面可以定义多个切点
     * 切点可以被后面的拦截方法使用，拦截方法并不局限于使用切点
     */
    @Pointcut(value = "@annotation(com.yuntwo.raindrop.annotation.UserAuthentication)")
    public void pointcut() {}

    /**
     * 与被注释方法正确返回之后执行
     * @param joinPoint 方法执行前的参数
     * @param result 方法返回值 后续观察，是否保存
     */
    @AfterReturning(returning = "result", value = "@annotation(com.yuntwo.raindrop.annotation.UserAuthentication)")
    public void after(JoinPoint joinPoint, Object result) {

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                HttpServletRequest request = (HttpServletRequest) arg;
                String token = request.getHeader("token");
                if(token == null){
                    log.warn("【登录校验】header中查不到token");
                    throw new UserAuthorizeException();
                }
                // 2. 查询redis  -- 根据token中的得到userId，如果token是伪造的话，就会查不到对应的redis
                String userId = stringRedisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX, token));
                if(userId == null) {
                    log.warn("【登录校验】Redis中查不到token");
                    throw new UserAuthorizeException();
                }
                // 3. 用户验证通过后，更新token在redis中的过期时间
                Long expireToken = stringRedisTemplate.getExpire(String.format(RedisConstant.TOKEN_PREFIX, token), TimeUnit.HOURS);
                // 过期时间只剩一个小时，更新expire
                if(expireToken == null){
                    log.warn("【登录校验】Redis中查不到token");
                    throw new UserAuthorizeException();
                }
                if(expireToken < 1) {
                    stringRedisTemplate.expire(String.format(RedisConstant.TOKEN_PREFIX, token),RedisConstant.EXPIRE_USER_LOGIN, TimeUnit.DAYS);
                    stringRedisTemplate.expire(String.format(RedisConstant.USERID_PREFIX, userId),RedisConstant.EXPIRE_USER_LOGIN, TimeUnit.DAYS);
                }

            }
        }

    }

    /**
     * Around在切点方法执行前后都会执行
     * @param proceedingJoinPoint
     * @param authentication
     * @return
     */
    @Around("pointcut() && @annotation(authentication)")
    public Object interceptor(ProceedingJoinPoint proceedingJoinPoint, UserAuthentication authentication){

        boolean pass = authentication.pass();
        //要验证权限
        AuthAopEnum role = authentication.role();
        if(pass && role !=  AuthAopEnum.ANON){
            //通过拿到的role,我们可以知道能处理这个请求的角色是什么
            //如果是匿名者，直接放行，如果是用户，就需要用户的权限才行
            //规定一致，token放在header中
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            assert attributes != null;
            HttpServletRequest request = attributes.getRequest();

            String token = request.getHeader("token");

            AuthAopEnum realRole = authenticate(token);

            if (realRole == role) {
                //权限正确
                try {
                    return proceedingJoinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    log.error("【用户鉴权异常:权限验证正确】exception:{}",throwable.getMessage());
                    return ResultVOUtil.error(ResultEnum.AOP_SERVER_ERROR.getCode(),ResultEnum.AOP_SERVER_ERROR.getMessage());
                }
            }else{
                //权限错误，返回错误：用户没有登录
                throw new UserAuthorizeException();
            }
        }else{
            //不验证权限
            try {
                return proceedingJoinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                log.error("【用户鉴权异常:不进行权限验证】exception:{}",throwable.getMessage());
                return ResultVOUtil.error(ResultEnum.AOP_SERVER_ERROR.getCode(),ResultEnum.AOP_SERVER_ERROR.getMessage());
            }
        }
    }

    /**
     * 这个辅助方法用于判断该token所属的到底是（ 用户？ 匿名？)
     * @param token
     * @return
     */
    private AuthAopEnum authenticate(String token){
        if(token == null) {
            return AuthAopEnum.ANON;
        }
        String userId = stringRedisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX, token));;

        if(userId == null) {
            //匿名的或者说用户过期的，没有找到session
            return AuthAopEnum.ANON;
        }
        return AuthAopEnum.USER;
    }
}
