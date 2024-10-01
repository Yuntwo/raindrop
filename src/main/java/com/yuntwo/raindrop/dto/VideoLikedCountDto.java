package com.yuntwo.raindrop.dto;

import lombok.Data;

@Data
public class VideoLikedCountDto {
    String videoId;
    Long count;

    public VideoLikedCountDto(String videoId, Long count) {
        this.videoId = videoId;
        this.count = count;
    }
}
