package com.oxchains.themis.common.reqlimit;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ccl
 * @time 2018-05-30 10:38
 * @name RequestLimitContract
 * @desc:
 */
@Aspect
@Component
public class RequestLimitContract {
    private static final Logger logger = LoggerFactory.getLogger("RequestLimitLoger");
    private  Map<String , Request> redisTemplate = null;
    @PostConstruct
    public void listeningMap(){
        redisTemplate = new HashMap<>();
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(10, new BasicThreadFactory.Builder().namingPattern("request-thead-%d").daemon(true).build());
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    if(redisTemplate != null && redisTemplate.size()>0){
                        for (Map.Entry<String,Request> m: redisTemplate.entrySet()) {
                            Request value = m.getValue();
                            if(System.currentTimeMillis() - value.getTime() >=  value.getLimit()){
                                redisTemplate.remove(m.getKey());
                            }
                        }
                    }

                } catch (Exception e) {
                    logger.error("listeningMap error:{}",e.getMessage(),e);
                }
            }
        }, 1,1, TimeUnit.MINUTES);
    }

    //@Before("@annotation(limit)")
    @Before("within(@org.springframework.stereotype.Controller *) && @annotation(limit)")
    public void requestLimit(final JoinPoint joinPoint, RequestLimit limit) throws RequestLimitException {
        try {
            Object[] args = joinPoint.getArgs();
            HttpServletRequest httpServletRequest = null;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof HttpServletRequest) {
                    httpServletRequest = (HttpServletRequest) args[i];
                    break;
                }
            }
            if (httpServletRequest == null) {
                return;
            }
            String ip = httpServletRequest.getRemoteAddr();
            String url = httpServletRequest.getRequestURL().toString();
            String key = "req_limit_".concat(url).concat(ip);
            if (redisTemplate.get(key) == null || redisTemplate.get(key).getCount() == 0) {
                redisTemplate.put(key, new Request(1,System.currentTimeMillis(),limit.time()));
            } else {
                redisTemplate.get(key).increase();
            }
            Request request = redisTemplate.get(key);
            if (request.getCount() > limit.count()) {
                logger.info("用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数[" + limit.count() + "]");
                long time = (request.getLimit() - (System.currentTimeMillis() - request.getTime())) / 1000;
                time = time < 1 ? 1 : time;
                throw new RequestLimitException(time);
            }
        }catch (RequestLimitException e){
            throw e;
        }catch (Exception e){
            logger.error("发生异常",e);
        }
    }
}
