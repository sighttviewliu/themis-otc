package com.oxchains.themis.repo.dao.aop;

import com.oxchains.themis.repo.entity.aop.OperationLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anonymity
 * @create 2018-07-25 13:28
 **/
@Repository
public interface OperationLogRepo extends CrudRepository<OperationLog, Long> {
}
