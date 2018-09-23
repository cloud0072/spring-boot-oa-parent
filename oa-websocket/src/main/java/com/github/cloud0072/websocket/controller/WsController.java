package com.github.cloud0072.websocket.controller;

import com.github.cloud0072.websocket.model.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * websocket 消息进出控制器
 */
@Controller
@Slf4j
public class WsController {

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/queue-chat")
    public void chat(Principal principal, ChatMessage msg) {
        log.info(principal + "\t:\t" + msg.toString());

        messagingTemplate.convertAndSendToUser(msg.getDestuser(), "/queue/chat", msg);
    }

    @MessageMapping("/topic-system")
    public void topic(Principal principal, ChatMessage msg) {
        log.info(principal + "\t:\t" + msg.toString());

        //广播使用convertAndSend方法，第一个参数为目的地，和js中订阅的目的地要一致
        messagingTemplate.convertAndSend("/topic/system", msg);
    }

    @RequestMapping("/chat/index")
    public String showChatPage() {
        return "/websocket/index";
    }
}