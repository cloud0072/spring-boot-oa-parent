package com.github.cloud0072.websocket.serivce;

import com.github.cloud0072.base.model.User;
import com.github.cloud0072.base.repository.BaseRepository;
import com.github.cloud0072.base.service.BaseCrudService;
import com.github.cloud0072.base.service.UserService;
import com.github.cloud0072.base.util.UserUtils;
import com.github.cloud0072.websocket.model.TopicRecord;
import com.github.cloud0072.websocket.repository.TopicRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: service
 * @Description: 发送的推送记录服务
 * @author caolei
 * @date 2018/9/25 12:59
 */
@Service
public class TopicRecordService implements BaseCrudService<TopicRecord> {
    @Autowired
    private TopicRecordRepository topicRecordRepository;
    @Autowired
    private UserService userService;

    @Override
    public BaseRepository<TopicRecord, String> repository() {
        return topicRecordRepository;
    }

    @Override
    public TopicRecord update(TopicRecord input,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        TopicRecord topicRecord = findById(input.getId());
        topicRecord.setContent(input.getContent());
        topicRecord.setTopic(input.getTopic());
        User user = userService.findById(UserUtils.getCurrentUser().getId());
        topicRecord.getCreator().modifyBy(user);
        return save(topicRecord, request, response);
    }

}
