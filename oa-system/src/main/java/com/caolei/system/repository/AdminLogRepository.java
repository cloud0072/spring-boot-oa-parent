package com.caolei.system.repository;

import com.caolei.system.pojo.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cloud0072
 */
@Repository
public interface AdminLogRepository extends JpaRepository<OperationLog,String> {
}
