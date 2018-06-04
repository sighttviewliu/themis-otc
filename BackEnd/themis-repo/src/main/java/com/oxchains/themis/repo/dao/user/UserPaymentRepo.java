package com.oxchains.themis.repo.dao.user;

import com.oxchains.themis.repo.entity.user.UserPayment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ccl
 * @time 2018-05-31 11:47
 * @name UserPaymentRepo
 * @desc:
 */
@Repository
public interface UserPaymentRepo extends CrudRepository<UserPayment,Long> {
    UserPayment findByUserId(Long userId);
}
