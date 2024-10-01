package com.yuntwo.raindrop.VO;

import lombok.Data;

import java.util.List;

@Data
public class PagedResultVO {
    /**
     * 当前页数
     */
    private int page;

    /**
     * 总页数
     */
    private int total;

    /**
     * 总记录数
     */
    private long records;

    /**
     * 每行显示的内容
     */
    private List<?> videos;
}
