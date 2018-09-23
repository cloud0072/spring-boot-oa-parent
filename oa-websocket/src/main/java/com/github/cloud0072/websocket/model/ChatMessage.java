package com.github.cloud0072.websocket.model;

import lombok.Data;

/**
 * 用户聊天请求
 * DTO 对象
 */
@Data
public class ChatMessage {

    //自身账号
    private String account;
    //目标用户
    private String destuser;
    //内容
    private String chatContent;

}
