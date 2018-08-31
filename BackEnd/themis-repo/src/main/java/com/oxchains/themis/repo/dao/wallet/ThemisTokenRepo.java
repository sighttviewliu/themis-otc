package com.oxchains.themis.repo.dao.wallet;

import com.oxchains.themis.repo.entity.wallet.ThemisToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ccl
 * @time 2018-06-28 11:27
 * @name ThemisTokenRepo
 * @desc:
 */
@Repository
public interface ThemisTokenRepo extends CrudRepository<ThemisToken,Long> {
    ThemisToken findByAddress(String address);
}
