package com.oxchains.themis.user.rest;

import com.oxchains.themis.common.auth.JwtService;
import com.oxchains.themis.common.model.RestResp;
import com.oxchains.themis.repo.entity.TokenKey;
import com.oxchains.themis.repo.entity.user.User;
import com.oxchains.themis.user.service.TokenKeyService;
import com.oxchains.themis.user.service.UserService;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ccl
 * @time 2017-10-13 10:32
 * @name TokenController
 * @desc:
 */
@RestController
@RequestMapping(value = "/token")
public class TokenController {
    @Resource
    JwtService jwtService;

    @Resource
    UserService userService;

    @Resource
    TokenKeyService tokenKeyService;

    @GetMapping(value = "/{id}")
    public RestResp token(@PathVariable Long id){
        return tokenKeyService.token(id);
    }

    @PostMapping(value = "/add")
    public RestResp token(@RequestBody TokenKey tokenKey){
        return tokenKeyService.token(tokenKey);
    }

    @PostMapping
    public RestResp token(User user){
        return userService.findUser(user).map(u -> {
          String token = jwtService.generate(u);
          return RestResp.success(token);
        }).orElse(RestResp.fail());
    }
}
