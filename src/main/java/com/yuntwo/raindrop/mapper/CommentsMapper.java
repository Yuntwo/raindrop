package com.yuntwo.raindrop.mapper;

import com.yuntwo.raindrop.VO.CommentWithUserVO;
import com.yuntwo.raindrop.entity.Comments;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentsMapper extends MyMapper<Comments> {

    /**
     * 增加收到的喜爱数
     * @param commentId
     */
    void addLikeNum(String commentId);

    /**
     * 减少收到的喜爱数
     * @param commentId
     */
    void reduceLikeNum(String commentId);

    /**
     * 根据评论id返回发布者id
     * @param commentId
     * @return
     */
    String queryPublisherIdByCommentId(String commentId);

    /**
     * 根据视频id获取评论列表
     * @param videoId
     * @return
     */
    List<CommentWithUserVO> queryCommentsByVideoId(String videoId);


    Comments queryCommentByCommentId(String commentId);
}
