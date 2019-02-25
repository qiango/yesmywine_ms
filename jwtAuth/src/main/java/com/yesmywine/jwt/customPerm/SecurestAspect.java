package com.yesmywine.jwt.customPerm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * Created by by on 2017/8/3.
 */
//@Aspect
//@Component
public class SecurestAspect {

    private  static  final Logger logger = LoggerFactory.getLogger(SecurestAspect. class);

    //Controller层切点 包路径
    @Pointcut("execution (* com.yesmywine.*.controller..*(..))")
    public  void controllerAspect() {
    }

    private String getOperation(String requestPerm,String userInfo) {
        String userPerms = ValueUtil.getFromJson(userInfo,"allPerms");
        JSONObject jsonObject = JSON.parseObject(userPerms);
        return jsonObject.getString(requestPerm);
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) throws ClassNotFoundException, YesmywineException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String perm = request.getHeader("RequestPerm");
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String value = "";
        String operationName = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    if(method.getAnnotation(SecurestValid.class)!=null){
                        value = method.getAnnotation(SecurestValid.class).value();
                        if(!perm.equals(value)){
                            ValueUtil.isError("您无此权限");
                        }
                    }
        //            String userInfo = RedisCache.get(Statement.USER_INFO+username);
        //            String operation = getOperation(perm,userInfo);
        //
        //            if(operation==null){
        //                ValueUtil.isError("请求权限 RequestPerm 参数，无效的操作");
        //            }
        //            LogThread logThread = new LogThread(username,operation);
        //            Threads thread = new Threads(logThread);
        //            thread.start();

                    break;
                }
            }
        }

        if(logger.isInfoEnabled()){
            logger.info("before " + joinPoint);
        }
    }

    //配置controller环绕通知,使用在方法aspect()上注册的切入点
    @Around("controllerAspect()")
    public Object around(JoinPoint joinPoint) throws ClassNotFoundException{
        long start = System.currentTimeMillis();
        try {
            Object obj = ((ProceedingJoinPoint) joinPoint).proceed();
            long end = System.currentTimeMillis();
            if(logger.isInfoEnabled()){
                logger.info("around " + joinPoint + "\tUse time : " + (end - start) + " ms!");
            }
            return obj;
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    /**
     * 后置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @After("controllerAspect()")
    public  void after(JoinPoint joinPoint) throws ClassNotFoundException, YesmywineException {

//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        HttpSession session = request.getSession();
//        String perm = request.getHeader("RequestPerm");
//        //读取session中的用户
//        // User user = (User) session.getAttribute("user");
//        //请求的IP
//        String ip = request.getRemoteAddr();
//
//        String targetName = joinPoint.getTarget().getClass().getName();
//        String methodName = joinPoint.getSignature().getName();
//        Object[] arguments = joinPoint.getArgs();
//        Class targetClass = Class.forName(targetName);
//        Method[] methods = targetClass.getMethods();
//        String value = "";
//        String operationName = "";
//        for (Method method : methods) {
//            if (method.getName().equals(methodName)) {
//                Class[] clazzs = method.getParameterTypes();
//                if (clazzs.length == arguments.length) {
////                    System.out.println(method.getAnnotation(SecurestValid.class));
////                    value = method.getAnnotation(SecurestValid.class).value();
////                        operationName = method.getAnnotation(SecurestAspect.class).operationName();
//                    break;
//                }
//            }
//        }
    }

    //配置后置返回通知,使用在方法aspect()上注册的切入点
    @AfterReturning("controllerAspect()")
    public void afterReturn(JoinPoint joinPoint) throws YesmywineException {
        if(logger.isInfoEnabled()){
            logger.info("afterReturn " + joinPoint);
        }
    }

    /**
     * 异常通知 用于拦截记录异常日志
     *
     * @param joinPoint
     * @param e
     */
//    @AfterThrowing(pointcut = "controllerAspect()", throwing="e")
//    public  String  doAfterThrowing(JoinPoint joinPoint, Throwable e) throws YesmywineException{
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        HttpSession session = request.getSession();
//        //读取session中的用户
//        //获取请求ip
//        String ip = request.getRemoteAddr();
//        //获取用户请求方法的参数并序列化为JSON格式字符串
//
//        String params = "";
//        if (joinPoint.getArgs() !=  null && joinPoint.getArgs().length > 0) {
//            for ( int i = 0; i < joinPoint.getArgs().length; i++) {
////                params += JsonUtil.getJsonStr(joinPoint.getArgs()[i]) + ";";
//                params += joinPoint.getArgs()[i] ;
//            }
//        }
//        try {
//
//            String targetName = joinPoint.getTarget().getClass().getName();
//            String methodName = joinPoint.getSignature().getName();
//            Object[] arguments = joinPoint.getArgs();
//            Class targetClass = Class.forName(targetName);
//            Method[] methods = targetClass.getMethods();
//            String operationType = "";
//            String operationName = "";
//            for (Method method : methods) {
//                if (method.getName().equals(methodName)) {
//                    Class[] clazzs = method.getParameterTypes();
//                    if (clazzs.length == arguments.length) {
//                        operationType = method.getAnnotation(SecurestValid.class).value();
////                        operationName = method.getAnnotation(Log.class).operationName();
//                        break;
//                    }
//                }
//            }
//             /*========控制台输出=========*/
//            System.out.println("=====异常通知开始=====");
//            System.out.println("异常代码:" + e.getClass().getName());
//            System.out.println("异常信息:" + e.getMessage());
//            System.out.println("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()")+"."+operationType);
//            System.out.println("方法描述:" + operationName);
////            System.out.println("请求人:" + user.getName());
////            System.out.println("请求IP:" + ip);
//            System.out.println("请求参数:" + params);
//               /*==========数据库日志=========*/
//            System.out.println("=====异常通知结束=====");
//        }  catch (Exception ex) {
//            //记录本地异常日志
//            logger.error("==异常通知异常==");
//            logger.error("异常信息:{}", ex.getMessage());
//        }
//         /*==========记录本地异常日志==========*/
//        logger.error("异常方法:{}异常代码:{}异常信息:{}参数:{}", joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage(), params);
//        return null;
//    }
}
