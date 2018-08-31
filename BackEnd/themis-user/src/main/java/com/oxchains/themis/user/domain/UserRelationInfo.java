package com.oxchains.themis.user.domain;

import com.oxchains.themis.repo.entity.user.User;
import com.oxchains.themis.repo.entity.user.UserRelation;
import com.oxchains.themis.repo.entity.user.UserTxDetail;
import lombok.Data;

/**
 * @author oxchains
 * @time 2017-11-28 10:20
 * @name UserRelationInfo
 * @desc:
 */
@Data
public class UserRelationInfo extends User {
    private UserRelation userRelation;
    private UserTxDetail userTxDetail;

    public UserRelationInfo(User user) {
        super(user);
    }

}
