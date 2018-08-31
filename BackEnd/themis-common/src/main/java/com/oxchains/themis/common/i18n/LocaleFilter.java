package com.oxchains.themis.common.i18n;

import org.apache.catalina.util.ParameterMap;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

/**
 * @author brandon
 * Created by brandon on 2018/4/16.
 * 国际化过滤器，用于在url传参过程中解析出url中的国际化参数
 */
//@Slf4j
//@Component
//@WebFilter("/*")
public class LocaleFilter implements Filter {

    @Value("${localeFilter.locales}")
    private String locales;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String[] locales = this.locales.split(",");

        ServletRequest localeRequest = new LocaleRequest((HttpServletRequest) request, locales);

        chain.doFilter(localeRequest, response);
    }

    @Override
    public void destroy() {
    }
}

class LocaleRequest extends HttpServletRequestWrapper {

    private String newUrl;
    private String newUri;
    private String newServletPath;
    private ParameterMap<String, String[]> params;

    @Override
    public StringBuffer getRequestURL() {
        return new StringBuffer(this.newUrl);
    }

    @Override
    public String getRequestURI() {
        return this.newUri;
    }

    @Override
    public String getServletPath() {
        return this.newServletPath;
    }

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public LocaleRequest(HttpServletRequest request, String[] locales) {
        super(request);
        init(request, locales);
    }

    /**
     * 初始化request的相关信息，将国际化参数从url中切割出来，并把url和uri重新生成新的request
     *
     * @param request
     * @param locales
     */
    private void init(HttpServletRequest request, String[] locales) {
        String contextPath = request.getContextPath();
        String url = new String(request.getRequestURL());
        String uri = request.getRequestURI().replaceAll(contextPath, "");
        String lang = uri.substring(0, uri.substring(uri.indexOf("/") + 1).indexOf("/") + 1);

        if (!checkLocale(locales, uri)) {
            lang = request.getSession().getAttribute("lang") ==  null? "" : (String) request.getSession().getAttribute("lang");
            if ("".equals(lang)) {
                Locale locale = request.getLocale();
                lang = locale.getLanguage() + "_" + locale.getCountry();
            }
        }

        this.params = (ParameterMap) request.getParameterMap();
        this.newUrl = url.replaceAll(lang, "");
        this.newServletPath = uri.replace(lang, "");
        this.newUri = contextPath + uri.replace(lang, "");
        lang = lang.replaceAll("/", "");
        this.addParameter("lang", lang);
        request.getSession().setAttribute("lang", lang);
    }


    /**
     * 校验uri中是否包含国际化参数，如果有返回true，没有则返回false
     *
     * @param locales
     * @param uri
     * @return
     */
    private boolean checkLocale(String[] locales, String uri) {
        boolean flag = false;
        for (String locale : locales) {
            flag = flag || uri.contains(locale);
        }
        return flag;
    }


    @Override
    public String getParameter(String name) {
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return params;
    }

    /**
     * 增加参数
     *
     * @param name
     * @param value
     */
    public void addParameter(String name, Object value) {
        if (value != null) {
            params.setLocked(false);
            if (value instanceof String[]) {
                params.put(name, (String[]) value);
            } else if (value instanceof String) {
                params.put(name, new String[]{(String) value});
            } else {
                params.put(name, new String[]{String.valueOf(value)});
            }
            params.setLocked(true);
        }
    }

}
