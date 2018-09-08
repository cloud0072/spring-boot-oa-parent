package com.caolei.system.service.impl;

import com.caolei.system.pojo.FileComponent;
import com.caolei.system.repository.FileComponentRepository;
import com.caolei.system.service.FileComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

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
    public JpaRepository<FileComponent, String> repository() {
        return fileComponentRepository;
    }

    @Override
    public FileComponent updateById(String id, FileComponent input) {
        return null;
    }
}
