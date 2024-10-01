package com.yuntwo.raindrop.VO.video;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * 学术视频描述
 */
@Data
@ApiModel
public class AcademicVideoUploadVO {

    @Size(max = 256)
    @ApiModelProperty(value = "视频标题",name = "title")
    private String title;

    @Size(max = 512)
    @ApiModelProperty(value = "视频描述",name = "description")
    private String description;

    @ApiModelProperty(value = "视频标签",name = "tag")
    private String tag;

    @ApiModelProperty(value = "视频是否公开，1公开，0私有",name = "isPublic")
    private Integer isPublic;

    @Size(max = 256)
    @ApiModelProperty(value = "视频相关网站", name = "websiteUrl")
    private String websiteUrl;
}
