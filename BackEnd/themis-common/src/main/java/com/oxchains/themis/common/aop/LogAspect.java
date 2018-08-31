package com.oxchains.themis.common.aop;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oxchains.themis.common.auth.AuthorizationConst;
import com.oxchains.themis.common.auth.JwtService;
import com.oxchains.themis.common.util.aop.IpUtils;
import com.oxchains.themis.common.util.aop.StringUtils;
import com.oxchains.themis.repo.dao.aop.ExceptionLogRepo;
import com.oxchains.themis.repo.dao.aop.OperationLogRepo;
import com.oxchains.themis.repo.entity.aop.ExceptionLog;
import com.oxchains.themis.repo.entity.aop.OperationLog;
import com.oxchains.themis.repo.entity.user.User;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;


/**
 * 日志切点
 *
 * @author anonymity
 * @create 2018-07-24 18:33
 **/
@Aspect
@Configuration
public class LogAspect {

    @Resource
    private OperationLogRepo operationLogRepo;
    @Resource
    private ExceptionLogRepo exceptionLogRepo;
    @Resource
    private JwtService jwtService;

    private final String paramUserIdKey = "userId";
    /**
     * 本地异常日志记录对象
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Controller层切点
     */
    @Pointcut("@annotation(com.oxchains.themis.common.aop.ControllerLogs)")
    public void controllerAspect() {
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            // 用户ID
            Long userId = getUserId(request);
            // 类名
            String className = joinPoint.getTarget().getClass().getName();
            // 方法参数
            Object[] pointArgs = joinPoint.getArgs();
//            String methodParam = JSON.toJSONString(pointArgs[0]);
            // 方法描述
            String methodDescription = getControllerMethodDescription(joinPoint);
            // 请求类型
            String requestType = request.getMethod();
            // 服务地址
            String serverAddress = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            // 远程地址
            String remoteAddress = IpUtils.getRemoteAddr(request);
            // 请求uri
            String requestUri = StringUtils.abbr(request.getRequestURI(), 255);
            // 参数中的userId
            String paramUserId = getUserIdFromParam(className, joinPoint);
            if(null == userId && null != paramUserId){
                userId = Long.parseLong(paramUserId);
            }
            // 保存用户操作日志
            OperationLog operationLog = new OperationLog(userId, paramUserId, requestType, methodDescription,
                    serverAddress, remoteAddress, request.getHeader("User-Agent"), requestUri, System.currentTimeMillis());
            operationLogRepo.save(operationLog);
        } catch (Exception e) {
            logger.error("doBefore failed", e);
        }
    }

    /**
     * 通过反射机制 获取被切参数名以及参数值
     *
     * @param cls
     * @param clazzName
     * @param methodName
     * @param args
     * @return
     * @throws NotFoundException
     */
    private Map<String, Object> getFieldsName(Class cls, String clazzName, String methodName, Object[] args) throws NotFoundException {
        Map<String, Object> map = new HashMap<String, Object>();

        ClassPool pool = ClassPool.getDefault();
        //ClassClassPath classPath = new ClassClassPath(this.getClass());
        ClassClassPath classPath = new ClassClassPath(cls);
        pool.insertClassPath(classPath);

        CtClass cc = pool.get(clazzName);
        CtMethod cm = cc.getDeclaredMethod(methodName);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            // exception
            return Collections.emptyMap();
        }
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < cm.getParameterTypes().length; i++) {
            map.put(attr.variableName(i + pos), args[i]);//paramNames即参数名
        }
        return map;
    }

    @AfterReturning(returning = "ret", pointcut = "controllerAspect()")
    public void doAfterReturning(Object ret) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //请求方法
        String method = StringUtils.abbr(request.getRequestURI(), 255);
    }

    /**
     * 异常通知 用于拦截service层记录异常日志
     */
    @AfterThrowing(pointcut = "controllerAspect()", throwing = "ex")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            // 类名
            String className = joinPoint.getTarget().getClass().getName();
            // 用户ID
            Long userId = getUserId(request);
            // 请求方法
            String method = (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()");
            // 方法参数
            Object[] pointArgs = joinPoint.getArgs();
//            String methodParam = JSON.toJSONString(pointArgs[0]);
            // 方法描述
            String methodDescription = getServiceMthodDescription(joinPoint);
            // 异常名称
            String exceptionName = ex.getClass().getName();
            // 异常信息
            String exceptionMessage = ex.getMessage();
            // 参数中的userId
            String paramUserId = getUserIdFromParam(className, joinPoint);
            if(null == userId && null != paramUserId){
                userId = Long.parseLong(paramUserId);
            }
            // 保存用户异常日志
            ExceptionLog exceptionLog = new ExceptionLog(userId, method, methodDescription,
                    exceptionName, exceptionMessage, System.currentTimeMillis());
            exceptionLogRepo.save(exceptionLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Long getUserId(HttpServletRequest request) {
        Long userId = null;
        String authorization = request.getHeader(AuthorizationConst.AUTHORIZATION_HEADER);
        if (authorization != null && authorization.startsWith(AuthorizationConst.AUTHORIZATION_START)) {
            User user = jwtService.parseToken(authorization.replaceAll(AuthorizationConst.AUTHORIZATION_START, ""));
            if (user != null) {
                userId = user.getId();
            }
        }
        return userId;
    }

    public String getUserIdFromParam(String className, JoinPoint joinPoint) throws NotFoundException {
        String paramUserId = null;
        Map<String, Object> paramKeyAndValue = getFieldsName(this.getClass(), className, joinPoint.getSignature().getName(), joinPoint.getArgs());
        // 请求url的参数中没有userId就从方法参数中获取
        if (paramKeyAndValue.containsKey(paramUserIdKey)) {
            paramUserId = paramKeyAndValue.get(paramUserIdKey) != null ? paramKeyAndValue.get(paramUserIdKey).toString() : null;
        } else {
                Object[] args = joinPoint.getArgs();
                if(null != args && args.length > 0){
                    for(Object obj : args){
                        if(!(obj instanceof HttpServletRequest)){
                            String jsonStr = JSON.toJSONString(obj);
                            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                            if (jsonObject.get(paramUserIdKey) != null){
                                paramUserId = jsonObject.get(paramUserIdKey).toString();
                            }

                        }
                    }
                }
      /*      if (paramKeyAndValue.containsKey(paramUserIdKey)) {
                JSONArray jsonArray = JSON.parseArray(methodParam);
                String json = jsonArray.get(0) != null ? jsonArray.get(0).toString() : null;
                JSONObject jsonObject = json != null ? JSONObject.parseObject(json) : null;
                paramUserId = jsonObject != null ? jsonObject.get(paramUserIdKey).toString() : null;
            }*/
        }
        //return null != paramUserId ? Long.valueOf(paramUserId) : null;
        return paramUserId;
    }
    /**
     * 获取注解中对方法的描述信息 用于service层注解
     */
    public String getServiceMthodDescription(JoinPoint joinPoint) throws Exception {
        return getMethodDesc(joinPoint);
    }

    public String getMethodDesc(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazz = method.getParameterTypes();
                if (clazz.length == arguments.length) {
                    description = method.getAnnotation(ControllerLogs.class).description();
                    break;
                }
            }
        }
        return description;
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     */
    public String getControllerMethodDescription(JoinPoint joinpoint) throws Exception {
        return getMethodDesc(joinpoint);
    }

    //获取客户端IP
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
