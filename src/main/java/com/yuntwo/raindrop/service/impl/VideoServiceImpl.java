package com.yuntwo.raindrop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.yuntwo.raindrop.VO.PagedResultVO;
import com.yuntwo.raindrop.VO.VideoWithUserVO;
import com.yuntwo.raindrop.VO.shield.ShieldedUserVO;

import com.yuntwo.raindrop.entity.UserLikeVideo;
import com.yuntwo.raindrop.entity.Videos;

import com.yuntwo.raindrop.enums.VideoTypeEnum;

import com.yuntwo.raindrop.mapper.*;

import com.yuntwo.raindrop.service.LikeService;
import com.yuntwo.raindrop.service.VideoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * Package short-video
 *
 * @author chenliquan on 2020/10/5 22:44
 * Description：
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideosMapper videosMapper;

    @Autowired
    private UsersAndVideosMapper usersAndVideosMapper;

    @Autowired
    private UserLikeVideoMapper userLikeVideoMapper;

    @Autowired
    private LikeService likeService;

    @Autowired
    private ShieldMapper shieldMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResultVO queryUserLikeVideoInfoByUserId(Integer page, Integer size, String userId) {
        PageHelper.startPage(page,size);
        List<VideoWithUserVO> videoWithUserVOS = usersAndVideosMapper.queryUserLikeVideoInfoByUserId(userId);
        return pageHelper(page, videoWithUserVOS);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<VideoWithUserVO> queryUserPublishVideoInfoByUserId(String userId) {
        return usersAndVideosMapper.queryUserPublishVideoInfoByUserId(userId);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<VideoWithUserVO> queryHottest10Video() {
        return usersAndVideosMapper.queryHottest10Video();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void createVideo(Videos video) {
        videosMapper.insertSelective(video);

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Videos findVideoInfoByVideoId(String videoId) {
        Example example = new Example(Videos.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("videoId",videoId);
        Videos video = videosMapper.selectOneByExample(example);
        return video;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateVideo(Videos video) {
        Example example = new Example(Videos.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("videoId",video.getVideoId());
        videosMapper.updateByExampleSelective(video,example);

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public VideoWithUserVO queryVideoByVideoId(String videoId) {
        return usersAndVideosMapper.queryVideoByVideoId(videoId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResultVO queryRecommendVideos(Integer page, Integer size) {
        PageHelper.startPage(page,size);

        List<VideoWithUserVO> videoList = usersAndVideosMapper.queryRecommendVideos();

        return pageHelper(page,videoList);
    }

    @Override
    public PagedResultVO queryRecommendVideos(Integer page, Integer size, String userId) {
        PageHelper.startPage(page,size);

        List<VideoWithUserVO> videoList = usersAndVideosMapper.queryRecommendVideos();

        List<ShieldedUserVO> shieldUserList = shieldMapper.querytoUserByFromUserId(userId);

        List<String> shieldUserIdList = new ArrayList<>();

        for(ShieldedUserVO shieldedUserVO : shieldUserList){
            shieldUserIdList.add(shieldedUserVO.getUserId());
        }


        videoList.removeIf(videoWithUserVO -> shieldUserIdList.contains(videoWithUserVO.getUserId()));

        return pageHelper(page,videoList);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResultVO queryVideosByTag(String tag, Integer page, Integer size) {
        PageHelper.startPage(page,size);
        List<VideoWithUserVO> videoList = usersAndVideosMapper.queryVideosByTag(tag);
        return pageHelper(page,videoList);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResultVO queryUserFollowVideosByUserId(String userId, Integer page, Integer size) {
        PageHelper.startPage(page,size);
        List<VideoWithUserVO> videoList = usersAndVideosMapper.queryUserFollowVideosByUserId(userId);
        return pageHelper(page,videoList);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResultVO searchVideosByTitleOrDesc(String searchContent, Integer page, Integer size) {
        PageHelper.startPage(page,size);
        List<VideoWithUserVO> videoList = usersAndVideosMapper.searchVideosByTitleOrDesc(searchContent);
        return pageHelper(page,videoList);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean isUserLikeVideo(String userId, String videoId) {
        Example example = new Example(UserLikeVideo.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("videoId", videoId);

        List<UserLikeVideo> list = userLikeVideoMapper.selectByExample(example);

        return list != null && list.size() > 0;
    }

    private  PagedResultVO pageHelper(Integer page,List<VideoWithUserVO> videoList) {

        for(VideoWithUserVO v: videoList){
            v.setLikeNum(likeService.queryLikeNumByVideoId(v.getVideoId(), VideoTypeEnum.COMMON_VIDEO.getCode()));
        }

        PageInfo<VideoWithUserVO> pageList = new PageInfo<>(videoList);

        PagedResultVO pagedResult = new PagedResultVO();

        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRecords(pageList.getTotal());
        pagedResult.setVideos(videoList);
        return pagedResult;
    }

    @Override
    public List<VideoWithUserVO> mostLikeNum() {
        return usersAndVideosMapper.mostLikeNum("#短视频大赛");
    }

    @Override
    public void computeScoreForVideos() {
        List<Videos> videosList = videosMapper.queryAllVideos();
        for(Videos videos : videosList){
            double timeScore = 0d;
            double timeParam = 0.001d;
            double likeScore = 0d;
            double likeParam = 0.001d;
            double score = 0d;
            double commonParam = 100d;

            long likeNum = videos.getLikeNum();

            //计算时间加权分数
            long createTime = videos.getCreateTime().getTime();
            long nowTime = System.currentTimeMillis();
            long timeInterval = (nowTime - createTime) / (1000 * 60 * 60); //小时

            timeScore = Math.exp(- timeInterval * timeParam);
            likeScore = 2 - Math.exp(- likeNum * likeParam);

            score = commonParam * timeScore * likeScore;


//            if(timeInterval < 1440){
//                timeScore = 100d;
//            }else if(timeInterval < 1440 * 3){
//                timeScore = 80d;
//            }else if(timeInterval < 1440 * 7){
//                timeScore = 60d;
//            }else if(timeInterval < 1440 * 15){
//                timeScore = 50d;
//            }else{
//                timeScore = 40d;
//            }
//
//            //计算点赞数加权分数
//            long likeNum = videos.getLikeNum();
//            if(likeNum <= 0){
//                likeScore = 0d;
//            }else if(likeNum <= 3){
//                likeScore = 20d;
//            }else if(likeNum <= 5){
//                likeScore = 30d;
//            }else if(likeNum <= 10){
//                likeScore = 40d;
//            }else if(likeNum <= 20){
//                likeScore = 50d;
//            }else if(likeNum <= 50){
//                likeScore = 60d;
//            }else if(likeNum < 100){
//                likeScore = 80d;
//            }else if(likeNum < 500){
//                likeScore = 100d;
//            }else{
//                likeScore = 150d;
//            }

//            score = timeScore + likeScore;

            videosMapper.updateScoreByVideoId(videos.getVideoId(), score);

        }
    }
}
