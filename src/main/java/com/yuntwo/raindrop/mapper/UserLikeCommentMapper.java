package com.yuntwo.raindrop.mapper;

import com.yuntwo.raindrop.entity.UserLikeComment;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserLikeCommentMapper extends MyMapper<UserLikeComment> {

    /**
     * 判断评论点赞记录是否存在
     * @return
     */
    UserLikeComment queryCommentLikeIsExists(String commentId, String userId);
}
