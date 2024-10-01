package com.yuntwo.raindrop.dto;

import lombok.Data;

@Data
public class UserLikedCountDto {
    String userId;
    Integer count;

    public UserLikedCountDto(String userId, Integer count) {
        this.userId = userId;
        this.count = count;
    }
}
