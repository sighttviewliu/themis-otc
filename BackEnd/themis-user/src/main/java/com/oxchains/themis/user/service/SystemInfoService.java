package com.oxchains.themis.user.service;

import com.oxchains.themis.repo.dao.NewsTipRepo;
import com.oxchains.themis.repo.dao.ProductRepo;
import com.oxchains.themis.repo.entity.BannerTip;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.repo.dao.BannerTipRepo;
import com.oxchains.themis.repo.dao.OrganizationRepo;
import com.oxchains.themis.repo.entity.NewsTip;
import com.oxchains.themis.repo.entity.Organization;
import com.oxchains.themis.repo.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ccl
 * @time 2018-05-07 14:07
 * @name SystemInfoService
 * @desc:
 */
@Slf4j
@Service
public class SystemInfoService {
    @Resource
    private OrganizationRepo organizationRepo;
    @Resource
    private NewsTipRepo newsTipRepo;

    @Resource
    private BannerTipRepo bannerTipRepo;

    @Resource
    private ProductRepo productRepo;
    public RestResp info(){
        return RestResp.success("success","welcome to visit themis");
    }

    public RestResp getOrganizationInfo(Long id){
        Organization organization = organizationRepo.findOrganizationById(id);
        return RestResp.success(organization);
    }

    public RestResp getNewsTip(){
        List<NewsTip> newsTipList = newsTipRepo.findByEnabled(1);
        return RestResp.success(newsTipList);
    }

    public RestResp getBannerTip(){
        List<BannerTip> bannerTipList = bannerTipRepo.findByEnabled(1);
        return RestResp.success(bannerTipList);
    }

    public RestResp getProductInfo(String code){
        Product product = productRepo.findProductByCode(code);
        return RestResp.success(product);
    }
}
