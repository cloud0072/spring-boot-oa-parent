package com.caolei.system.repository;

import com.caolei.common.api.repository.BaseRepository;
import com.caolei.system.pojo.OperationLog;
import org.springframework.stereotype.Repository;

/**
 * @author cloud0072
 */
@Repository
public interface OperationLogRepository extends BaseRepository<OperationLog, String> {
}
