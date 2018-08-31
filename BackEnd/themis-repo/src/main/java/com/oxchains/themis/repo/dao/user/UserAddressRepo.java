package com.oxchains.themis.repo.dao.user;

import com.oxchains.themis.repo.entity.user.UserAddress;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author ccl
 * @time 2018-06-04 13:32
 * @name UserAddressRepo
 * @desc:
 */
public interface UserAddressRepo extends CrudRepository<UserAddress,Long> {
    List<UserAddress> findByUserId(Long userId);
    List<UserAddress> findByUserIdOrderByTypeAscCreateTimeDesc(Long userId);
    List<UserAddress> findByUserIdAndTypeOrderByCreateTimeDesc(Long userId, Integer type);
    UserAddress findByAddress(String address);
    UserAddress findByUserIdAndAddress(Long userId, String address);
    List<UserAddress> findByAddressIn(List<String> addresses);

    int deleteByAddress(String address);
}
