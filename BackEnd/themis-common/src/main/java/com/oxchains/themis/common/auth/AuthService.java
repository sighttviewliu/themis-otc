package com.oxchains.themis.common.auth;

import com.oxchains.themis.repo.entity.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author ccl
 * @time 2018-05-30 10:20
 * @name AuthService
 * @desc:
 */
@Component
public class AuthService {
    public boolean checkUserAuth(Long userId){
        if(null == userId){
            return false;
        }
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        if(null != authentication){
            User user = (User) authentication.getPrincipal();
            if(null != user && userId.equals(user.getId())){
                return true;
            }
        }
        return false;
    }
}
