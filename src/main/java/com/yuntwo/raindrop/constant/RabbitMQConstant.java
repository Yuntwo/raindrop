package com.yuntwo.raindrop.constant;

/**
 * 在RabbitMQ中会用到的 exchange，queue，key
 * 就是说把一些业务逻辑用到的配置全部集合成一个常数类这种代码组织很方便
 * 当然这只是一些用到的常量，不涉及到RabbitMQ的具体操作
 */
public interface RabbitMQConstant {

    String EXCHANGE_VERIFY_CODE = "exchange.direct.verify-code";

    String EXCHANGE_DIRECT_REPORT = "exchange.direct.report";

    String EXCHANGE_DIRECT_LIKE = "exchange.direct.like";

    String QUEUE_VERIFY_CODE = "queue.verify-code";

    String QUEUE_REPORT = "queue.report";

    String QUEUE_LIKE = "queue.like";

    String ROUTING_KEY_DIRECT_VERIFY_CODE = "routing-key.direct.verify-code";

    String ROUTING_KEY_DIRECT_REPORT = "routing-key.direct.report";

    String ROUTING_KEY_DIRECT_LIKE = "routing-key.direct.like";


}
