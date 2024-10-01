package com.yuntwo.raindrop.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 加载application.yml中阿里云的配置信息到Java类中，使其可以被注入应用到其他类中
 */
@Component
@Data
public class AliyunSendSmsProperties {

    /**
     * \@Value可以从配置文件中找到键为 "aliyun.sms.accessKeyId" 的配置，然后将这个配置的值赋给 accessKeyId 这个字段
     */
    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.sms.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.sms.template_code}")
    private String templateCode;

    @Value("${aliyun.sms.sign_name}")
    private String signName;
}
