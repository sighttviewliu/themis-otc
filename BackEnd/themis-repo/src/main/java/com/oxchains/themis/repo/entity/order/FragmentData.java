package com.oxchains.themis.repo.entity.order;

import lombok.Data;

import java.util.List;

/**
 * 秘钥碎片
 * @author anonymity
 * @create 2018-08-20 14:22
 **/
@Data
public class FragmentData {
    private String min_rebuild_num;
    private List<ShareData> share_data;
    private List<String> verify_data;
}
