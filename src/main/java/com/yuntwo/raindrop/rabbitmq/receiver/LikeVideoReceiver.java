package com.yuntwo.raindrop.rabbitmq.receiver;

import com.yuntwo.raindrop.constant.RabbitMQConstant;
import com.yuntwo.raindrop.rabbitmq.message.LikeVideoMessage;
import com.yuntwo.raindrop.service.LikeService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.EncodeException;
import java.io.IOException;

/**
 *
 */

@Service
public class LikeVideoReceiver {

    @Autowired
    private LikeService likeService;

    @RabbitHandler
    @RabbitListener(queues = RabbitMQConstant.QUEUE_LIKE)
    public void processLikeVideo(LikeVideoMessage likeVideoMessage) throws IOException, EncodeException {
        String userId = likeVideoMessage.getUserId();
        if(likeVideoMessage.isLike()){
            likeService.likeVideo(likeVideoMessage.getVideoId(),userId);
        } else{
            likeService.unlikeVideo(likeVideoMessage.getVideoId(),userId);
        }
    }
}
