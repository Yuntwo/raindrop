package com.yuntwo.raindrop.mapper;

import com.yuntwo.raindrop.VO.video.AcademicVideoWithUserVO;
import com.yuntwo.raindrop.entity.AcademicVideos;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AcademicVideosMapper extends MyMapper<AcademicVideos> {

    AcademicVideos queryAcademicVideoByVideoIdAndStatusAndIsPublic(String videoId, Integer status, Integer isPublic);

    AcademicVideos queryAcademicVideoByVideoIdAndStatus(String videoId, Integer status);

    Long queryLikeNumByVideoId(String videoId);

    List<AcademicVideos> getRecommendAcademicVideo();

    AcademicVideos queryAcademicVideoByVideoId(String videoId);

    AcademicVideoWithUserVO queryAcademicVideoWithUserVOByVideoId(String videoId);

    List<AcademicVideoWithUserVO> queryRecommendAcademicVideoWithUserVO();

    void addCommentNum(String videoId);

    List<AcademicVideos> queryAllVideos();

    void updateScoreByAcademicVideoId(String academicVideoId, Double score);
}
