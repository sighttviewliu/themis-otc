package com.oxchains.themis.user.domain;

import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ccl
 * @time 2018-06-28 13:46
 * @name Address
 * @desc:
 */
@Data
public class Address {
    private String name;
    private List<Map<String, String>> address;


    public Address(String name, List<Map<String, String>> address) {
        this.name = name;
        this.address = address;
    }
}
