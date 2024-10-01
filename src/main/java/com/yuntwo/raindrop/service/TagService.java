package com.yuntwo.raindrop.service;

import java.util.List;

/**
 * 查询标签
 */
public interface TagService {

    /**
     * 查询所有的视频分类的标签
     * @return
     */
    List<String> queryAllVideoTag();

    /**
     * 查询所有的举报的标签
     * @return
     */
    List<String> queryAllReportTag();
}

