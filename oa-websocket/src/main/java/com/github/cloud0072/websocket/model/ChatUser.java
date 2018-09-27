package com.github.cloud0072.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.Principal;

/**
 * @ClassName: ChatUser
 * @Description: stomp连接时的信息
 * @author caolei
 * @date 2018/9/27 14:17
 */
@AllArgsConstructor
@Data
public class ChatUser implements Principal {

    private String username;

    @Override
    public String getName() {
        return username;
    }


}
