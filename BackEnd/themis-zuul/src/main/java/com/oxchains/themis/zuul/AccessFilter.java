package com.oxchains.themis.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.oxchains.themis.zuul.service.ParseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author ccl
 * @time 2017-11-09 17:36
 * @name AccessFilter
 * @desc:
 */
@Slf4j
@Component
public class AccessFilter extends ZuulFilter {

    private static String urlWhitelistString;

    private static ArrayList<String> urlWhitelist;

    @Resource
    private ParseService parseService;

    /**
     * pre：请求执行之前的filter
     * route：处理请求，进行路由
     * post：请求处理完成后执行的filter
     * error：出现错误是执行的filter
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * filter执行顺序，通过数字指定，优先级,数字越大,优先级越低
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * filter是否需要执行，true：执行，false：不执行
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * filter具体逻辑
     */
    @Override
    public Object run() {
        try {
            RequestContext rcx = RequestContext.getCurrentContext();
            HttpServletRequest request = rcx.getRequest();

            log.info("Send {} Request to {}", request.getMethod(),request.getRequestURL());

            String url = request.getRequestURI();
            boolean inWhitelist = isWhiteUrl(url);
            String token = request.getHeader("Authorization");
            log.info("Authorization: {}", token);
            if (null == token) {
                log.info("This Request Without TOKEN");
                if (inWhitelist) {
                    log.info("URL: {} in whitelist", url);
                } else {
                    //过滤该请求，不往下级服务去转发请求，到此结束
                    response401(rcx);
                    return null;
                }
            } else {
                log.info("This Request contains TOKEN ：{}", token);
                boolean isSuccess = parseService.parse(token);
                if (isSuccess || inWhitelist) {
                    log.info("Request Success");
                } else {
                    response401(rcx);
                    return null;
                }
            }
            //如果有token，则进行路由转发
            log.info("Authorized,continue...");
            //这里return的值没有意义，zuul框架没有使用该返回值
            return null;
        } catch (Exception e) {
            log.error("Zuul filter 异常", e);
        }
        return null;
    }

    private void response401(RequestContext rcx){
        rcx.setSendZuulResponse(false);
        rcx.setResponseStatusCode(401);
        rcx.setResponseBody("{}");
        log.error("请求无效");
    }

    private boolean isWhiteUrl(String str){
        if(null == str || "".equals(str.trim()) || null == AccessFilter.urlWhitelist || AccessFilter.urlWhitelist.size()<1){
            return false;
        }
        for(String url : AccessFilter.urlWhitelist){
            if(str.equals(url) || str.contains(url)){
                return true;
            }
        }
        return false;
    }

    public static String getUrlWhitelistString() {
        return urlWhitelistString;
    }

    @Value("${themis.zuul.url.whitelist}")
    public void setUrlWhitelistString(String urlWhitelistString) {
        AccessFilter.urlWhitelistString = urlWhitelistString;

        AccessFilter.urlWhitelist = new ArrayList<>();
        String[] urls = AccessFilter.urlWhitelistString.split(",");
        for(String url : urls){
            AccessFilter.urlWhitelist.add(url);
        }

    }
}
