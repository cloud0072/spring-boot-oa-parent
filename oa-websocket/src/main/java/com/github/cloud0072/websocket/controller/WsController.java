package com.github.cloud0072.websocket.controller;

import com.github.cloud0072.base.controller.BaseController;
import com.github.cloud0072.base.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * websocket 消息进出控制器
 */
@Controller
@Slf4j
public class WsController implements BaseController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

//    @MessageMapping("/topic/chat")
//    public void userChat(UserChatCommand userChat) {
//        //找到需要发送的地址
//        String dest = "/queue/chat" + userChat.getCoordinationId();
//        //发送用户的聊天记录
//        this.messagingTemplate.convertAndSend(dest, userChat);
//    }

    @MessageMapping("/topic/chat")
    public void test(String message) {
        log.info(UserUtils.getCurrentUser().getUserName() + ":\t" + message);
        //找到需要发送的地址
        String dest = "/queue/chat";
        //发送用户的聊天记录
        this.messagingTemplate.convertAndSend(dest, message);
    }

}