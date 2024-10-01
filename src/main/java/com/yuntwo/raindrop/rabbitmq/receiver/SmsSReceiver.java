package com.yuntwo.raindrop.rabbitmq.receiver;

import com.aliyuncs.exceptions.ClientException;
import com.yuntwo.raindrop.constant.RabbitMQConstant;
import com.yuntwo.raindrop.util.aliyun.SmsSUtil;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 这个包下面其实就是消息队列中消息的处理，这里的receiver含义其实就接近于consumer
 * 这是对queue-verify-code中消息进行处理，解耦于主逻辑吧，概念类似于微服务了，可以拆分的
 */
@Component
public class SmsSReceiver {

    @Autowired
    private SmsSUtil smsSUtill;

    @Value("${aliyun.sms.template_code}")
    private String templateCode;

    @Value("${aliyun.sms.sign_name}")
    private String signName;

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = RabbitMQConstant.QUEUE_VERIFY_CODE,durable = "true"),
        exchange = @Exchange(name = RabbitMQConstant.EXCHANGE_VERIFY_CODE),
        key = RabbitMQConstant.ROUTING_KEY_DIRECT_VERIFY_CODE
    ))
    @RabbitHandler
    public void executeSmsS(Map<String,String> map) {
        String phone = map.get("phone");
        String verifyCode = map.get("verifyCode");
        System.out.println("===============开始发送验证码==============");

        // todo : code需要根据短信的模板变化
        try{
            smsSUtill.sendSms(phone,templateCode,signName,"{\"code\":\""+verifyCode+"\"}");
        }catch (ClientException e) {
            e.printStackTrace();
        }

    }
}
