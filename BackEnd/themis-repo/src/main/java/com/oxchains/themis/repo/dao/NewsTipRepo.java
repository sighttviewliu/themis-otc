package com.oxchains.themis.repo.dao;

import com.oxchains.themis.repo.entity.BannerTip;
import com.oxchains.themis.repo.entity.NewsTip;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ccl
 * @time 2018-07-12 11:50
 * @name NewsTipRepo
 * @desc:
 */
@Repository
public interface NewsTipRepo extends CrudRepository<NewsTip,Long> {
    List<NewsTip> findByEnabled(Integer enabled);

}
