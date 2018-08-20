package com.caolei.system.repository;

import com.caolei.system.pojo.FileComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: FileComponentRepository
 * @Description: TODO
 * @author caolei
 * @date 2018/8/20 10:12
 */
@Repository
public interface FileComponentRepository extends JpaRepository<FileComponent, String> {
}
