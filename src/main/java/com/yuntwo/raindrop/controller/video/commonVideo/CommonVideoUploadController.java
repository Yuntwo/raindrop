package com.yuntwo.raindrop.controller.video.commonVideo;

import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.yuntwo.raindrop.VO.ResultVO;
import com.yuntwo.raindrop.VO.video.VideoUpdateVO;
import com.yuntwo.raindrop.annotation.UserAuthentication;
import com.yuntwo.raindrop.entity.Videos;
import com.yuntwo.raindrop.enums.AuthAopEnum;
import com.yuntwo.raindrop.enums.ResultEnum;
import com.yuntwo.raindrop.enums.VideoStatusEnum;
import com.yuntwo.raindrop.service.TagService;
import com.yuntwo.raindrop.service.VideoService;
import com.yuntwo.raindrop.util.FastDfsUtil;
import com.yuntwo.raindrop.util.RedisUtil;
import com.yuntwo.raindrop.util.ResultVOUtil;
import com.yuntwo.raindrop.util.aliyun.VideoPlayUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * 上传普通视频接口
 */
@Api(value = "上传普通视频接口", tags = {"上传普通视频"})
@RestController
@RequestMapping("/commonVideo")
@Slf4j
public class CommonVideoUploadController {
    @Autowired
    private VideoService videoService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TagService tagService;

    @Autowired
    private FastDfsUtil fastDfsUtil;


    /**
     * 异步方法，Spring保证等待CompletableFuture对象得到实际的返回值后再将其包装为HttpResponse返回给前端
     * 如果直接是同步方法返回ResultVO，Spring直接将其包装为HttpResponse返回给前端，Spring有不同的处理机制
     * 但是返回给前端还是同步等待结果，所以后端这里异步还是同步只是对后端系统性能的影响，对于前端来说没有区别
     * @param videoUpdateVO
     * @param request
     * @return
     */
    @ApiOperation(value = "获取上传视频的凭证",notes = "/commonVideo/getVideoUploadAuth")
    @PostMapping("/getVideoUploadAuth")
    @UserAuthentication(pass = true,role = AuthAopEnum.USER)
    public CompletableFuture<ResultVO<Object>> getVideoUploadAuth(@Validated @RequestBody VideoUpdateVO videoUpdateVO, HttpServletRequest request) {
        if (StringUtils.isEmpty(videoUpdateVO.getTitle()) || StringUtils.isEmpty(videoUpdateVO.getTag()) || org.springframework.util.StringUtils.isEmpty((videoUpdateVO.getIsPublic()))) {
            log.error("【获取视频上传凭证错误】输入参数不完整，videoUpdateVO: {}", videoUpdateVO.getTitle() + "," + videoUpdateVO.getTag() + "," + videoUpdateVO.getIsPublic());
            return CompletableFuture.completedFuture(ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage()));
        }

        // thenApply和exceptionally都会包裹里面函数的返回值为一个CompletableFuture对象，通过这个对象获取返回值
        // 但是里面函数的执行会在另外的线程执行，也就是说不会阻塞当前线程后面语句的执行，结果通过Completable对象异步获取
        // Java的多线程模型可能是开启子线程执行内部函数，如果是JavaScript的单线程模型，就是放入事件循环队列执行
        return VideoPlayUtil.createUploadVideoAsync(videoUpdateVO).thenApply(response -> {
            HashMap<String, String> map = new HashMap<>();
            map.put("VideoId", response.getVideoId());
            map.put("UploadAddress", response.getUploadAddress());
            map.put("UploadAuth", response.getUploadAuth());
            Videos video = new Videos();
            BeanUtils.copyProperties(videoUpdateVO, video);
            video.setVideoId(response.getVideoId());
            video.setUserId(redisUtil.getUserIdByToken(request));
            video.setVideoDesc(videoUpdateVO.getDescription());
            video.setStatus(VideoStatusEnum.UPLOADING.getCode());

            videoService.createVideo(video);

            return ResultVOUtil.success(map);
        }).exceptionally(ex -> {
            log.error("【获取视频上传凭证异常】", ex);
            return ResultVOUtil.error(ResultEnum.INTERNAL_SERVER_ERROR.getCode(), ResultEnum.INTERNAL_SERVER_ERROR.getMessage());
        });
    }



    @ApiOperation(value = "上传普通视频",notes = "/commonVideo/upload")
    @ApiImplicitParams({
        @ApiImplicitParam(name="videoId", value="视频主键id", required=true,
            dataType="String", paramType="form")
    })
    @PostMapping("/upload")
    @UserAuthentication(pass = true,role = AuthAopEnum.USER)
    public ResultVO upload(String videoId,
                           @ApiParam(value="视频封面", required=true) MultipartFile coverFile) throws Exception {

        if(StringUtils.isEmpty(videoId)) {
            log.error("【视频上传】输入参数不完整，videoUpdateVO: {}",videoId + "+" + coverFile);
            return ResultVOUtil.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }

        // 查询视频是否存在
        Videos video = videoService.findVideoInfoByVideoId(videoId);

        if(video == null) {
            log.error("【视频上传】视频不存在，videoId: {}",videoId);
            return ResultVOUtil.error(ResultEnum.VIDEO_IS_NOT_EXIST.getCode(),ResultEnum.VIDEO_IS_NOT_EXIST.getMessage());
        }

        try {
            String path = fastDfsUtil.uploadFile(coverFile);
            if (!org.springframework.util.StringUtils.isEmpty(coverFile)) {
                video.setVideoCoverPath(path);
            } else {
                return ResultVOUtil.error(ResultEnum.UPDATE_VIDEO_COVER_ERROR.getCode(),ResultEnum.UPDATE_VIDEO_COVER_ERROR.getMessage());
            }
        } catch (Exception e) {
            log.error("【上传视频封面】服务异常，Exception: {}", e.getMessage());
            return ResultVOUtil.error(ResultEnum.UPDATE_VIDEO_COVER_ERROR.getCode(),ResultEnum.UPDATE_VIDEO_COVER_ERROR.getMessage());
        }
        // 更新视频信息

        // video.setStatus(VideoStatusEnum.SUCCESS.getCode());

        videoService.updateVideo(video);

        return ResultVOUtil.successMsg(ResultEnum.UPLOAD_VIDEO_SUCCESS.getMessage());
    }

}

