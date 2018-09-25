package com.github.cloud0072.websocket.repository;

import com.github.cloud0072.base.repository.BaseRepository;
import com.github.cloud0072.websocket.model.TopicRecord;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: TopicRecordRepository
 * @Description: 发送的推送记录
 * @author caolei
 * @date 2018/9/25 12:58
 */
@Repository
public interface TopicRecordRepository extends BaseRepository<TopicRecord, String> {

}
