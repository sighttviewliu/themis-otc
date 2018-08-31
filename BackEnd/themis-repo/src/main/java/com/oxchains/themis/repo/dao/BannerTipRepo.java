package com.oxchains.themis.repo.dao;

import com.oxchains.themis.repo.entity.BannerTip;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ccl
 * @time 2018-07-12 11:50
 * @name BannerTipRepo
 * @desc:
 */
@Repository
public interface BannerTipRepo extends CrudRepository<BannerTip,Long> {
    List<BannerTip> findByEnabled(Integer enabled);

}
