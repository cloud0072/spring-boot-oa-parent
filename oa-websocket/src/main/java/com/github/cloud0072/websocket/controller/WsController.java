package com.github.cloud0072.websocket.controller;

import com.github.cloud0072.common.util.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * websocket 消息进出控制器
 */
@Controller
@Slf4j
public class WsController {

    @Autowired
    SimpMessagingTemplate messagingTemplate;
    @Autowired
    private SimpUserRegistry userRegistry;

    @MessageMapping("/queue-chat")
    public void chat(Principal principal, String message) {
        Map<String, Object> body = JSONUtils.readValue(message, Map.class);
        log.info("{}:\t{}", principal, message);
        Object destUser = body.get("destUser");
        if (destUser != null) {
            //广播使用convertAndSend方法，第一个参数为目的地，和js中订阅的目的地要一致
            messagingTemplate.convertAndSendToUser(destUser.toString(), "/queue/chat", message);
        }
    }

    @MessageMapping("/topic-system")
    public void topic(Principal principal, String message) {
        Map<String, Object> body = JSONUtils.readValue(message, Map.class);
        List list = (List) body.get("userGroup");
        log.info("{}:\t{}", principal, message);
        //把用户组的值作为destination的参数进行遍历广播 这里暂时没有实现
        log.info("{}:\t{}", "userGroup", list);
        //广播使用convertAndSend方法，第一个参数为目的地，和js中订阅的目的地要一致
        messagingTemplate.convertAndSend("/topic/system", message);
    }

    @RequestMapping("/chat/index")
    public String showChatPage() {
        return "/websocket/index";
    }

    @RequestMapping("/ws/onlineUserInfo")
    public ResponseEntity onlineUserInfo() {
        log.info("当前在线人数:" + userRegistry.getUserCount());
        Set<SimpUser> users = userRegistry.getUsers();
        for (SimpUser user : users) {
            log.info(user.toString());
        }
        return ResponseEntity.ok(users);
    }

}