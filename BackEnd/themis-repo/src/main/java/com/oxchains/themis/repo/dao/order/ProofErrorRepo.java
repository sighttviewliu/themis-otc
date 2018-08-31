package com.oxchains.themis.repo.dao.order;

import com.oxchains.themis.repo.entity.order.ProofError;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author anonymity
 * @create 2018-08-15 11:14
 **/
public interface ProofErrorRepo extends CrudRepository<ProofError, Long> {

    List<ProofError> findByPush(boolean push);
}
