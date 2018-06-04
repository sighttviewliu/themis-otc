package com.oxchains.themis.repo.dao.user;

import com.oxchains.themis.repo.entity.user.UserQIC;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ccl
 * @time 2018-05-29 18:31
 * @name UserQICDao
 * @desc:
 */
@Repository
public interface UserQICDao extends CrudRepository<UserQIC, Long> {

    UserQIC findByUserId(Long userId);
    UserQIC findByIdNo(String idNo);
}
