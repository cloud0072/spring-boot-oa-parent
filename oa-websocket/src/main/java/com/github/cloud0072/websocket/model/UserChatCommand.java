package com.github.cloud0072.websocket.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户聊天请求
 * DTO 对象
 */
@Getter
@Setter
public class UserChatCommand {

    private String name;
    private String chatContent;
    private String coordinationId;

    @Override
    public String toString() {
        return "UserChatCommand{" +
                "name='" + name + '\'' +
                ", chatContent='" + chatContent + '\'' +
                ", coordinationId='" + coordinationId + '\'' +
                '}';
    }

}
