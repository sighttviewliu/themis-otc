package com.oxchains.themis.blockinfo;

import java.io.IOException;
import java.util.Date;

/**
 * @author oxchains
 * @time 2018-05-23 18:53
 * @name Test
 * @desc:
 */
public class Test {
    public static void main(String[] args) throws IOException {
        System.out.println("0x80dabe63c879505f9859b828e133325ac818a68276d097ff145cd962dd76784e".length());
        System.out.println("0x0127eb89ff5bdd96af11b7e4e01cda03f22b28e1".length());
        Date date = new Date(1527140701000L);
        System.out.println(date);
        System.out.println(System.currentTimeMillis());
        Long ll = Long.valueOf("5b06515d",16);
        System.out.println(ll);

        Web3jHandler.getInstance("http://192.168.1.205:8545").getBalance("0x0127eb89ff5bdd96af11b7e4e01cda03f22b2811");
    }
}
