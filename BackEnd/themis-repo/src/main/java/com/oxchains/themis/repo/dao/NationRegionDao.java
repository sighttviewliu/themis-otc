package com.oxchains.themis.repo.dao;

import com.oxchains.themis.repo.entity.NationRegion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ccl
 * @time 2018-05-15 10:50
 * @name NationRegionDao
 * @desc:
 */
@Repository
public interface NationRegionDao extends CrudRepository<NationRegion,Long> {
}
