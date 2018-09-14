package com.oxchains.themis.repo.dao.user;

import com.oxchains.themis.repo.entity.user.UserPayment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ccl
 * @time 2018-05-31 11:47
 * @name UserPaymentRepo
 * @desc:
 */
@Repository
public interface UserPaymentRepo extends CrudRepository<UserPayment,Long> {
    List<UserPayment> findByUserId(Long userId);
    List<UserPayment> findByUserIdAndPtype(Long userId, Integer ptype);
    List<UserPayment> findByUserIdAndPtypeAndEnabled(Long userId, Integer ptype, Integer enabled);
    List<UserPayment> findByUserIdAndEnabled(Long userId, Integer enabled);
    UserPayment findByUserIdAndPayment(Long userId, String payment);
    UserPayment findUserPaymentById(Long id);
}
