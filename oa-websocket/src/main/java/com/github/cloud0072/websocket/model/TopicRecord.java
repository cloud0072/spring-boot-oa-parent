package com.github.cloud0072.websocket.model;

import com.github.cloud0072.base.model.BaseEntity;
import com.github.cloud0072.base.model.extend.Creator;
import com.github.cloud0072.common.module.SocketModuleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

/**
 * @ClassName: TopicRecord
 * @Description: 发送的推送记录
 * @author caolei
 * @date 2018/9/25 12:48
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
@Entity
public class TopicRecord
        extends BaseEntity
        implements SocketModuleEntity {
    /**
     * 内容
     */
    private String content;
    /**
     * 话题目标
     */
    private String topic;
    /**
     * 创建者
     */
    private Creator creator;


}
