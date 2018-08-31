package com.oxchains.themis.repo.dao.user;

import com.oxchains.themis.repo.entity.user.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author ccl
 * @time 2017-10-12 17:20
 * @name UserDao
 * @desc:
 */
@Repository
public interface UserDao extends CrudRepository<User,Long>, JpaSpecificationExecutor {
    /**
     * find by loginname
     * @param loginname
     * @return User
     */
    User findByLoginname(String loginname);

    User findByEmail(String email);

    /**
     * 通过手机查找
     * @param mobilephone
     * @return User
     */
    User findByMobilephone(String mobilephone);

    /**
     * find by login and password
     * @param loginname
     * @param password
     * @return Optional<User>
     */
    Optional<User> findByLoginnameAndPassword(String loginname, String password);
    Optional<User> findByLoginnameAndPasswordAndEnabled(String loginname, String password,Integer enabled);

    /**
     * find by email and password
     * @param loginname
     * @param password
     * @return Optional<User>
     */
    Optional<User> findByEmailAndPassword(String loginname, String password);
    Optional<User> findByEmailAndPasswordAndEnabled(String loginname, String password, Integer enabled);

    /**
     * find by phone and password
     * @param loginname
     * @param password
     * @return Optional<User>
     */
    Optional<User> findByMobilephoneAndPassword(String loginname, String password);
    Optional<User> findByMobilephoneAndPasswordAndEnabled(String loginname, String password,Integer enabled);


    List<User> findByRoleId(Long roleId);

    List<User> findByIdIn(List<Long> ids);

    User findUserById(Long id);

    List<User> findByLoginnameLike(String loginname);
}
