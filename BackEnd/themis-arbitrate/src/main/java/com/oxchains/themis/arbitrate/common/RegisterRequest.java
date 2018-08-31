package com.oxchains.themis.arbitrate.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;

@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RegisterRequest {
    private MultipartFile multipartFile;
    private String id;
    private Long userId;
    @Size(max = 200, message = "上限200字")
    private String content;
    private String fileName;
    private String image;
    private MultipartFile[] files;


}
