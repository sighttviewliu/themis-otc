package com.oxchains.themis.blockinfo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ccl
 * @time 2018-05-22 11:38
 * @name InfoController
 * @desc:
 */
@RestController
public class InfoController {
    @GetMapping(value = "/welcome")
    public String welcome() {
        return "Welcome to blockchain browser";
    }
}
