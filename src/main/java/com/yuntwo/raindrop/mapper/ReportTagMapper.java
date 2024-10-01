package com.yuntwo.raindrop.mapper;

import com.yuntwo.raindrop.entity.ReportTag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 举报标签
 */
@Mapper
public interface ReportTagMapper extends MyMapper<ReportTag> {

    /**
     * 查找所有举报的tag
     * @return
     */
    List<String> queryAllReportTag();
}

