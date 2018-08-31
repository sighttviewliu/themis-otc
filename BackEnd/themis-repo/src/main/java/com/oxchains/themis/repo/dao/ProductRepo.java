package com.oxchains.themis.repo.dao;


import com.oxchains.themis.repo.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ccl
 * @time 2018-07-12 11:48
 * @name OrganizationRepo
 * @desc:
 */
@Repository
public interface ProductRepo extends CrudRepository<Product,Long> {
    Product findProductById(Long id);
    Product findProductByCode(String code);
}
