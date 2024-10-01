package com.yuntwo.raindrop.VO.video;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.Size;

/**
 * 学术视频更新描述
 */
@Data
@ApiModel
public class AcademicVideoUpdateVO {

    @ApiModelProperty(value = "学术视频id", name = "academicVideoId")
    private String videoId;

    @Size(max = 256)
    @ApiModelProperty(value = "视频标题", name = "title")
    private String title;

    @Size(max = 512)
    @ApiModelProperty(value = "视频简介", name = "videoDesc")
    private String description;

    @Size(max = 256)
    @ApiModelProperty(value = "视频相关网站", name = "websiteUrl")
    private String websiteUrl;

    @ApiModelProperty(value = "视频是否公开", name = "isPublic")
    private Integer isPublic;

    @ApiModelProperty(value = "视频标签", name = "tag")
    private String tag;
}

