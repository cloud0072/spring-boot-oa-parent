package com.github.cloud0072.base.service.impl;

import com.github.cloud0072.base.model.FileComponent;
import com.github.cloud0072.base.repository.FileComponentRepository;
import com.github.cloud0072.base.service.FileComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.cloud0072.base.repository.BaseRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author caolei
 * @ClassName: FileComponentServiceImpl
 * @Description: TODO
 * @date 2018/8/20 10:15
 */
@Service
public class FileComponentServiceImpl
        implements FileComponentService {

    @Autowired
    private FileComponentRepository fileComponentRepository;

    @Override
    public BaseRepository<FileComponent, String> repository() {
        return fileComponentRepository;
    }

    @Override
    public FileComponent update(FileComponent input,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        return null;
    }

}
