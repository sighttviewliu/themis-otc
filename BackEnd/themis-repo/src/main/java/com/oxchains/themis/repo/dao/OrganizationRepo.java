package com.oxchains.themis.repo.dao;

import com.oxchains.themis.repo.entity.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ccl
 * @time 2018-07-12 11:48
 * @name OrganizationRepo
 * @desc:
 */
@Repository
public interface OrganizationRepo extends CrudRepository<Organization,Long> {
    Organization findOrganizationById(Long id);
}
