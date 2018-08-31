package com.oxchains.themis.common.param;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ccl
 * @time 2018-07-02 17:30
 * @name FileVO
 * @desc:
 */
@Data
public class FileVO {
    private String name;
    private MultipartFile file;
}
