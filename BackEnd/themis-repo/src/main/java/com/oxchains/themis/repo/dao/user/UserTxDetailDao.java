package com.oxchains.themis.repo.dao.user;

import com.oxchains.themis.repo.entity.user.UserTxDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ccl
 * @time 2017-10-30 19:00
 * @nameUserTxDetailDao
 * @desc:
 */
@Repository
public interface UserTxDetailDao  extends CrudRepository<UserTxDetail,Long>{
    UserTxDetail findByUserId(Long userId);
}
