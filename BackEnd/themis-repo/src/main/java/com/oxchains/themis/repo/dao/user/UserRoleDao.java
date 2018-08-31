package com.oxchains.themis.repo.dao.user;

import com.oxchains.themis.repo.entity.user.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ccl
 * @time 2017-12-12 17:10
 * @name UserRepo
 * @desc:
 */
@Repository
public interface UserRoleDao extends CrudRepository<UserRole,Long> {

    List<UserRole> findByUserId(Long userId);
    List<UserRole> findByRoleId(Long roleId);
    UserRole findByUserIdAndRoleId(Long userId, Long roleId);

    int countByUserId(Long userId);
    int countByRoleId(Long roleId);
}
