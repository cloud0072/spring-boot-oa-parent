package com.github.cloud0072.websocket.controller;

import com.github.cloud0072.common.util.JSONUtils;
import com.github.cloud0072.websocket.model.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Map;

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
    public void topic(Principal principal, String message) {
        Map<String, Object> body = JSONUtils.readValue(message, Map.class);

        log.info("{}:\t{}", principal, message);
        log.info("{}:\t{}", "userGroup", body.get("userGroup"));
        //广播使用convertAndSend方法，第一个参数为目的地，和js中订阅的目的地要一致
        messagingTemplate.convertAndSend("/topic/system", message);
    }

    @RequestMapping("/chat/index")
    public String showChatPage() {
        return "/websocket/index";
    }
}