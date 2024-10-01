package com.yuntwo.raindrop.controller.welcome;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 返回隐私策略页面，这里是用thymeleaf视图模版引擎渲染的html页面，也可以用配置其他模版引擎进行渲染
 * 这些模版渲染引擎可以根据string找到对应的view
 */
@Controller
public class PolicyController {

    @RequestMapping("/policy")
    public String policy() {
        return "privacy";
    }
}
