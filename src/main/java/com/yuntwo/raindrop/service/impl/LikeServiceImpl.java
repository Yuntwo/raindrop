package com.yuntwo.raindrop.service.impl;


import com.yuntwo.raindrop.VO.LikeCommentVO;
import com.yuntwo.raindrop.VO.remind.CommentLikeRemindVO;
import com.yuntwo.raindrop.VO.remind.VideoLikeRemindVO;
import com.yuntwo.raindrop.VO.video.LikeVideoVO;

import com.yuntwo.raindrop.constant.RedisConstant;

import com.yuntwo.raindrop.dto.UserLikedCountDto;
import com.yuntwo.raindrop.dto.VideoLikedCountDto;

import com.yuntwo.raindrop.entity.*;

import com.yuntwo.raindrop.enums.VideoStatusEnum;
import com.yuntwo.raindrop.enums.VideoTypeEnum;

import com.yuntwo.raindrop.mapper.*;

import com.yuntwo.raindrop.service.LikeService;
import com.yuntwo.raindrop.service.RedisService;

import com.yuntwo.raindrop.websocket.WebSocketServer;

import lombok.extern.slf4j.Slf4j;

import org.n3r.idworker.Sid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class LikeServiceImpl implements LikeService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private VideosMapper videosMapper;

    @Autowired
    private AcademicVideosMapper academicVideosMapper;

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private UserLikeVideoMapper userLikeVideoMapper;

    @Autowired
    private UserLikeCommentMapper userLikeCommentMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 添加喜爱视频信息到MySQL
     * 用事务来进行处理以避免失败问题
     * @param videoId
     * @param userId userId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void likeVideo(String videoId, String userId) throws IOException, EncodeException {
        // 生成点赞id
        // String likeId = UUIDUtil.getId();
        String likeId = Sid.next();
        UserLikeVideo ulv = userLikeVideoMapper.queryVideoLikeIsExists(videoId,userId);
        if(ulv == null){
            UserLikeVideo userLikeVideo = new UserLikeVideo();
            userLikeVideo.setId(likeId);
            userLikeVideo.setUserId(userId);
            userLikeVideo.setVideoId(videoId);
            userLikeVideo.setCreateTime(new Date());

            userLikeVideoMapper.insert(userLikeVideo);
        }
    }

    /**
     * 推送点赞消息到视频发布者
     * @param likeVideoVO
     * @param userId
     * @throws IOException
     * @throws EncodeException
     */
    @Override
    public void likeVideoRemind(LikeVideoVO likeVideoVO, String userId) throws IOException, EncodeException {

        Integer videoType = (likeVideoVO.getType() == 1) ? VideoTypeEnum.ACADEMIC_VIDEO.getCode() : VideoTypeEnum.COMMON_VIDEO.getCode();
        String videoId = likeVideoVO.getVideoId();

        String title, publisherId;

        if(videoType.equals(VideoTypeEnum.ACADEMIC_VIDEO.getCode())){
            AcademicVideos academicVideos = academicVideosMapper.queryAcademicVideoByVideoId(videoId);
            title = academicVideos.getTitle();
            publisherId = academicVideos.getUserId();

        } else {
            Videos videos = videosMapper.queryById(videoId);
            title = videos.getTitle();
            // 推送至视频发布者
            publisherId = videos.getUserId();
        }

        String username = usersMapper.queryUserByUserId(userId).getUsername();

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);

        if(!publisherId.equals(userId)) {
            if (webSocketServer.isOnline(publisherId)) {
                VideoLikeRemindVO videoLikeRemindVO = new VideoLikeRemindVO();
                videoLikeRemindVO.setType(3);
                videoLikeRemindVO.setVideoId(videoId);
                videoLikeRemindVO.setTitle(title);
                videoLikeRemindVO.setUserId(userId);
                videoLikeRemindVO.setUsername(username);
                videoLikeRemindVO.setCreateTime(new Date());
                webSocketServer.sendMessageToUser(videoLikeRemindVO, publisherId);

            } else {
                String hashkey = publisherId + "::" + Sid.next();
                String hashValue = "3" + "::" + videoId + "::" + title + "::" + userId + "::" + username + "::" + (dateString);
                redisTemplate.opsForHash().put(RedisConstant.REMIND_VIDEO_LIKE, hashkey, hashValue);
            }
        }

    }

    /**
     * 添加取消喜爱视频信息
     * 用事务来进行处理以避免失败问题
     * @param videoId
     * @param userId userId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void unlikeVideo(String videoId, String userId){

        // 删喜爱表数据
        Example example = new Example(UserLikeVideo.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("videoId", videoId);

        userLikeVideoMapper.deleteByExample(example);

    }

    @Override
    public void likeVideoNum(LikeVideoVO likeVideoVO, String userId) {

        Integer videoType = (likeVideoVO.getType() == 1) ? VideoTypeEnum.ACADEMIC_VIDEO.getCode() : VideoTypeEnum.COMMON_VIDEO.getCode();

        String videoId = likeVideoVO.getVideoId();

        String publisherId;
        if(videoType.equals(VideoTypeEnum.ACADEMIC_VIDEO.getCode())){
            publisherId = academicVideosMapper.queryAcademicVideoByVideoId(videoId).getUserId();
        }else{
            publisherId = videosMapper.queryPublisherIdByVideoId(videoId);
        }

        if(!redisService.hashKeyExist(RedisConstant.MAP_KEY_VIDEO_LIKED_COUNT, videoId)){
//            Long videoLikeNum = videosMapper.queryLikeNumByVideoId(videoId);
            Long videoLikeNum = queryLikeNumFromDB(videoId, videoType);
            redisService.setHash(RedisConstant.MAP_KEY_VIDEO_LIKED_COUNT, videoId, videoLikeNum);
        }
        if(!redisService.hashKeyExist(RedisConstant.MAP_KEY_USER_LIKED_COUNT,publisherId)){
            Integer userLikeNum = usersMapper.queryLikeNumByUserId(publisherId);
            redisService.setHash(RedisConstant.MAP_KEY_USER_LIKED_COUNT, publisherId, userLikeNum);
        }
        redisService.changeVideoLikedCount(RedisConstant.MAP_KEY_VIDEO_LIKED_COUNT, videoId, 1);
        redisService.changeUserLikedCount(RedisConstant.MAP_KEY_USER_LIKED_COUNT, publisherId, 1);
    }


    @Override
    public void unlikeVideoNum(LikeVideoVO likeVideoVO, String userId) {
        String videoId = likeVideoVO.getVideoId();

        Integer videoType = (likeVideoVO.getType() == 1) ? VideoTypeEnum.ACADEMIC_VIDEO.getCode() : VideoTypeEnum.COMMON_VIDEO.getCode();

//        String publisherId = videosMapper.queryPublisherIdByVideoId(videoId);

        String publisherId;
        if(videoType.equals(VideoTypeEnum.ACADEMIC_VIDEO.getCode())){
            publisherId = academicVideosMapper.queryAcademicVideoByVideoId(videoId).getUserId();
        }else{
            publisherId = videosMapper.queryPublisherIdByVideoId(videoId);
        }

//        Long videoLikeNum = videosMapper.queryLikeNumByVideoId(videoId);
//        Integer userLikeNum = usersMapper.queryLikeNumByUserId(publisherId);

        if(!redisService.hashKeyExist(RedisConstant.MAP_KEY_VIDEO_LIKED_COUNT, videoId)){
//            Long videoLikeNum = videosMapper.queryLikeNumByVideoId(videoId);
            Long videoLikeNum = queryLikeNumFromDB(videoId, videoType);
            redisService.setHash(RedisConstant.MAP_KEY_VIDEO_LIKED_COUNT, videoId, videoLikeNum);
        }
        if(!redisService.hashKeyExist(RedisConstant.MAP_KEY_USER_LIKED_COUNT,publisherId)){
            Integer userLikeNum = usersMapper.queryLikeNumByUserId(publisherId);
            redisService.setHash(RedisConstant.MAP_KEY_USER_LIKED_COUNT, publisherId, userLikeNum);
        }

        redisService.changeVideoLikedCount(RedisConstant.MAP_KEY_VIDEO_LIKED_COUNT, videoId, -1);
        redisService.changeUserLikedCount(RedisConstant.MAP_KEY_USER_LIKED_COUNT, publisherId, -1);
    }

    /**
     * 添加喜爱评论信息
     * @param likeCommentVO likeCommentVO
     * @param userId userId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void likeComment(LikeCommentVO likeCommentVO, String userId) throws IOException, EncodeException {
        // 1. 保存点赞关系表
        // String starId = UUIDUtil.getId();
        String starId = Sid.next();
        String commentId = likeCommentVO.getCommentId();
        UserLikeComment userLikeComment = new UserLikeComment();
        userLikeComment.setId(starId);
        userLikeComment.setUserId(userId);
        userLikeComment.setCommentId(commentId);
        userLikeComment.setCreateTime(new Date());


        //1. 推送给被点赞用户
        Comments comments = commentsMapper.queryCommentByCommentId(commentId);
        String publisherId = comments.getFromUserId();
        String videoId = commentsMapper.queryCommentByCommentId(commentId).getVideoId();

        Integer videoType = (likeCommentVO.getType() == 1) ? VideoTypeEnum.ACADEMIC_VIDEO.getCode() : VideoTypeEnum.COMMON_VIDEO.getCode();

        String title;

        if(videoType.equals(VideoTypeEnum.ACADEMIC_VIDEO.getCode())){
            AcademicVideos academicVideos = academicVideosMapper.queryAcademicVideoByVideoId(videoId);
            title = academicVideos.getTitle();
        }else{
            Videos videos = videosMapper.queryById(videoId);
            title = videos.getTitle();
        }

        String oriContext = comments.getContent();
        String username = usersMapper.queryUserByUserId(userId).getUsername();

        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);

        if(!publisherId.equals(userId)) {
            if (webSocketServer.isOnline(publisherId)) {
                CommentLikeRemindVO commentLikeReminderVO = new CommentLikeRemindVO();
                commentLikeReminderVO.setType(1);
                commentLikeReminderVO.setVideoId(videoId);
                commentLikeReminderVO.setTitle(title);
                commentLikeReminderVO.setCommentId(commentId);
                commentLikeReminderVO.setOriCommentContext(oriContext);
                commentLikeReminderVO.setUserId(userId);
                commentLikeReminderVO.setUsername(username);
                commentLikeReminderVO.setCreateTime(currentTime);

                webSocketServer.sendMessageToUser(commentLikeReminderVO, publisherId);
            } else {
                String hashkey = publisherId + "::" + Sid.next();
                String hashValue = "1" + "::" + videoId + "::" + title + "::" + commentId + "::" + oriContext + "::" + userId + "::" + username + "::" + (dateString);
                redisTemplate.opsForHash().put(RedisConstant.REMIND_COMMENT_LIKE, hashkey, hashValue);

            }
        }

        // 2. 保存点赞关系表
        userLikeCommentMapper.insert(userLikeComment);

        // 3. 评论喜欢数量增加
        commentsMapper.addLikeNum(commentId);

    };

    /**
     * 添加取消喜爱评论信息
     * @param likeCommentVO likeCommentVO
     * @param userId userId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void unlikeComment(LikeCommentVO likeCommentVO, String userId){
        // 1. 删喜爱表数据
        String commentId = likeCommentVO.getCommentId();
        Example example = new Example(UserLikeComment.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("commentId", commentId);

        userLikeCommentMapper.deleteByExample(example);

        // 2. 评论喜欢数减少
        commentsMapper.reduceLikeNum(commentId);
    };

    /**
     * 判断视频点赞记录是否存在
     * @param videoId videoId
     * @param userId userId
     * @return boolean
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryVideoLikeIsExists(String videoId, String userId){

        UserLikeVideo userLikeVideo = userLikeVideoMapper.queryVideoLikeIsExists(videoId, userId);
        return userLikeVideo != null;
    };

    /**
     * 判断评论点赞记录是否存在
     * @return boolean
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryCommentLikeIsExists(String commentId, String userId){

        UserLikeComment userLikeComment = userLikeCommentMapper.queryCommentLikeIsExists(commentId, userId);
        return userLikeComment != null;
    };

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transLikedCountFromRedis2DB() {
        List<VideoLikedCountDto> list1 = redisService.getLikedCountFromRedis();
        for (VideoLikedCountDto dto : list1) {
            Videos videos = videosMapper.queryById(dto.getVideoId());
            //点赞数量属于无关紧要的操作，出错无需抛异常
            if (videos != null){
                videosMapper.setLikeNumByVideoId(dto.getVideoId(),dto.getCount());
            }
        }

        List<UserLikedCountDto> list2 = redisService.getUserLikedCountFromRedis();
        for (UserLikedCountDto dto : list2) {
            Users users = usersMapper.queryUserByUserId(dto.getUserId());
            //点赞数量属于无关紧要的操作，出错无需抛异常
            if (users != null){
                usersMapper.setLikeNumByUserId(dto.getUserId(), dto.getCount());
            }
        }
    }

    @Override
    @Transactional
    public Long queryLikeNumByVideoId(String videoId, Integer videoType){
        if(redisTemplate.opsForHash().hasKey(RedisConstant.MAP_KEY_VIDEO_LIKED_COUNT, videoId)){
            Object o = redisTemplate.opsForHash().get(RedisConstant.MAP_KEY_VIDEO_LIKED_COUNT, videoId);
            return Long.valueOf(String.valueOf(o));
        }
        if(videoType.equals(VideoTypeEnum.COMMON_VIDEO.getCode())) {
            Videos videos = videosMapper.queryById(videoId);
            if (videos == null) {
                return 0L;
            }
            return videosMapper.queryLikeNumByVideoId(videoId);
        } else if(videoType.equals(VideoTypeEnum.ACADEMIC_VIDEO.getCode()) ){
            AcademicVideos academicVideos = academicVideosMapper.queryAcademicVideoByVideoIdAndStatus(videoId, VideoStatusEnum.SUCCESS.getCode());
            if(academicVideos == null){
                return 0L;
            }
            return academicVideosMapper.queryLikeNumByVideoId(videoId);
        }
        return 0L;

    }

    @Override
    @Transactional
    public Integer queryLikeNumByUserId(String userId) {
        if(redisTemplate.opsForHash().hasKey(RedisConstant.MAP_KEY_USER_LIKED_COUNT, userId)){
            Object o = redisTemplate.opsForHash().get(RedisConstant.MAP_KEY_USER_LIKED_COUNT, userId);
            return Integer.valueOf(String.valueOf(o));
        }
        Users users = usersMapper.queryUserByUserId(userId);
        if(users == null) {
            return 0;
        }
        return usersMapper.queryLikeNumByUserId(userId);
    }


    @Override
    public Long queryLikeNumFromDB(String videoId, Integer videoType) {
        if(videoType.equals(VideoTypeEnum.COMMON_VIDEO.getCode())){
            return videosMapper.queryLikeNumByVideoId(videoId);
        }else if(videoType.equals(VideoTypeEnum.ACADEMIC_VIDEO.getCode())){
            return academicVideosMapper.queryLikeNumByVideoId(videoId);
        }

        return 0L;
    }
}
