package com.oxchains.themis.blockinfo.ethereum.listener;

import com.oxchains.themis.blockinfo.ethereum.entity.EthBlockInfo;
import com.oxchains.themis.blockinfo.ethereum.repo.EthBlockInfoRepo;
import lombok.extern.slf4j.Slf4j;
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

    private static String SERVER_URL = "http://192.168.1.205:8545";
    protected final Web3j web3j = Web3j.build(new HttpService(SERVER_URL));

    @Resource
    EthBlockInfoRepo ethBlockInfoRepo;
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Subscription subscription = web3j.blockObservable(false).subscribe(ethBlock -> {
            EthBlock.Block block = ethBlock.getBlock();
            EthBlockInfo blockInfo = EthBlockInfo.block2BlockInfo(block);
            log.info("{}",blockInfo);
            ethBlockInfoRepo.save(blockInfo);
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}
