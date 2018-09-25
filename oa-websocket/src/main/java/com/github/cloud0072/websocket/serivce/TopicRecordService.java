package com.github.cloud0072.websocket.serivce;

import com.github.cloud0072.base.repository.BaseRepository;
import com.github.cloud0072.base.service.BaseCrudService;
import com.github.cloud0072.websocket.model.TopicRecord;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: service
 * @Description: TODO
 * @author caolei
 * @date 2018/9/25 12:59
 */
@Service
public class TopicRecordService implements BaseCrudService<TopicRecord> {
    @Override
    public BaseRepository<TopicRecord, String> repository() {
        return null;
    }

    @Override
    public TopicRecord update(TopicRecord input, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

}
