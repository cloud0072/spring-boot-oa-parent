package com.caolei.base.repository;

import com.caolei.common.api.repository.BaseRepository;
import com.caolei.base.pojo.OperationLog;
import org.springframework.stereotype.Repository;

/**
 * @author cloud0072
 */
@Repository
public interface OperationLogRepository extends BaseRepository<OperationLog, String> {
}
