package com.oxchains.themis.notice.dao.average;

import com.oxchains.themis.notice.domain.rate.HKDDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: Gaoyp
 * @Description:
 * @Date: Create in 下午6:59 2018/7/7
 * @Modified By:
 */
@Repository
public interface HKDDetailDao extends CrudRepository<HKDDetail,Long> {

    HKDDetail findByName(String name);

}
