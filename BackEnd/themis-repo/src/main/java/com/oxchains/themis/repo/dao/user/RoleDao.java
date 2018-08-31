package com.oxchains.themis.repo.dao.user;

import com.oxchains.themis.repo.entity.user.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ccl
 * @time 2017-10-26 10:16
 * @name RoleDao
 * @desc:
 */
@Repository
public interface RoleDao extends CrudRepository<Role,Long> {
    /**
     * find role info by id
     * @param id
     * @return
     */
    Role findById(Long id);


    List<Role> findByIdIn(List<Long> ids);
}
