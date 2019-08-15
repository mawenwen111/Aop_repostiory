package com.example.demo.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.aopalliance.intercept.Joinpoint;
import org.apache.ibatis.javassist.*;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.apache.tomcat.jni.Poll;
import org.aspectj.lang.JoinPoint;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class RequestUtil {
    public static String getRequestIp(HttpServletRequest request){
        if(request == null){

        }
        String ip = request.getHeader("x-forwarded-for");
        if(ip==null || ip.trim()=="" ||"unknown".equalsIgnoreCase(ip)){
            ip=request.getHeader("Proxy-Client-IP");
        }
        if(ip==null || ip.trim()==""||"unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip==null || ip.trim()==""||"unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
       final String[] arr = ip.split(",");
        for (final String str : arr){
            if(!"unknown".equalsIgnoreCase(str)){
                ip = str;
                break;
            }
        }
        return ip;
    }
    public static  Integer getRequestType(HttpServletRequest request){
        if(request==null){
            throw new RuntimeException();
        }
        String xRequestWith = request.getHeader("X-Requested-With");
        return xRequestWith == null?1:2;
    }
    public  static Map<Object,String> getJoinPointInfoMap(JoinPoint joinPoint){
        Map<Object, String> joinPointInfo = new HashMap<>();
        String classPath = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        joinPointInfo.put("classPath",classPath);
        joinPointInfo.put("methodName",methodName);
        Class<?> clazz = null;
        CtMethod ctMethod = null;
        LocalVariableAttribute attr = null;
        int length = 0;
        int pos = 0;
        try {
            clazz = Class.forName(classPath);
            String clazzName = clazz.getName();
            ClassPool pool = ClassPool.getDefault();
            ClassClassPath classClassPath = new ClassClassPath(clazz);
            pool.insertClassPath(classClassPath);
            CtClass ctClass = pool.get(clazzName);
            ctMethod = ctClass.getDeclaredMethod(methodName);
            MethodInfo methodInfo = ctMethod.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if(attr==null){
                return joinPointInfo;
            }
            length=ctMethod.getParameterTypes().length;
            pos= Modifier.isStatic(ctMethod.getModifiers())?0:1;
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (NotFoundException e){
            e.printStackTrace();
        }
        Map<Object, Object> paramMap = new HashMap<>();
        Object[] paramsArgsValues = joinPoint.getArgs();
        String[] paramsArgsNames = new String[length];
        for (int i = 0;i<length;i++){
            paramsArgsNames[i] = attr.variableName(i + pos);
            String paramArgsName = attr.variableName(i + pos);
            if(paramArgsName.equalsIgnoreCase("request")||
                paramArgsName.equalsIgnoreCase("response")||
                    paramArgsName.equalsIgnoreCase("session")){
                break;
            }
            Object paramsArgsValue = paramsArgsValues[i];
            paramMap.put(paramArgsName,paramsArgsValue);
        }
        joinPointInfo.put("paramMap", JSON.toJSONString(paramMap, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue));
        return joinPointInfo;

    }
}
