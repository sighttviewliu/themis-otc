package com.oxchains.themis.blockinfo.ethereum.listener;

import com.oxchains.themis.blockinfo.ethereum.entity.EthBlockInfo;
import com.oxchains.themis.blockinfo.ethereum.entity.ResParam;
import com.oxchains.themis.blockinfo.ethereum.repo.EthBlockInfoRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;
import rx.Subscription;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @author ccl
 * @time 2018-05-22 15:39
 * @name EthListener
 * @desc:
 */

@Slf4j
@WebListener
public class EthListener implements ServletContextListener{
    /*@Service
public class EthListener implements ApplicationListener{
*/


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }


}

