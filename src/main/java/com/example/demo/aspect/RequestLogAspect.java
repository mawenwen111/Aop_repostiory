package com.example.demo.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.DemoApplication;
import com.example.demo.annotation.Annotation;
import com.example.demo.mapper.ExceptionMapper;
import com.example.demo.mapper.RequestMapper;
import com.example.demo.pojo.ExceptionLog;
import com.example.demo.pojo.RequestLog;
import com.example.demo.util.RequestUtil;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 然后编写一个AOP切面类，
 * 由于使用的SpringBoot只要保证在Application子目录之下就被会自动扫描，所以只需要注解声明即可，并不需要额外配置
 * @Aspect声明这是一个切面类
 */
@Aspect
@Component
public class RequestLogAspect {
    private RequestLog requestLog= new RequestLog();
    private ExceptionLog exceptionLog = new ExceptionLog();
    private  long startTime;
    private long returnTime;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private RequestMapper requestMapper;
    @Autowired
    private ExceptionMapper exceptionMapper;

    /**
     * 定义请求日志切入点
     * 此处切入点为所有声明@Operation注解的方法
     * @param operation
     */
    @Pointcut(value = "@annotation(operation)")
    public void serviceStatistics(DemoApplication.Operation operation){

    }

    /**
     * 编写方法的前置通知
     * 前置通知在执行目标方法之前执行
     * 在前置通知中设置请求日志信息，如开始时间，请求参数，注解内容等
     * @param joinPoint
     * @param operation
     */
    @Before("serviceStatistics(operation)")
    public void doBefore(JoinPoint joinPoint, DemoApplication.Operation operation){
        Map<Object, String> joinPointInfo = RequestUtil.getJoinPointInfoMap(joinPoint);
        startTime = System.currentTimeMillis();
        Date date = new Date(startTime);
//        date.setTime(startTime);
//        String format = new SimpleDateFormat().format(date);
        requestLog.setStartTime(date);
        requestLog.setIp(RequestUtil.getRequestIp(request));
        requestLog.setClassPath(joinPointInfo.get("classPath").toString());
        requestLog.setMethodName(joinPointInfo.get("methodName").toString());
        requestLog.setWay(request.getMethod());
        requestLog.setParam((String)joinPointInfo.get("paramMap"));
        requestLog.setType(RequestUtil.getRequestType(request));
        requestLog.setSessionId(request.getSession().getId());
        requestLog.setUrl(request.getRequestURL().toString());
        requestLog.setOperation(operation.value());
        requestLog.setId(UUID.randomUUID().toString());
        //requestMapper.insert(requestLog);
    }

    /**
     * 编写方法的返回通知
     * 在目标方法正常结束之后执行
     * 在返回通知中补充请求日志信息，如返回时间，方法耗时，返回值，并且保存日志信息
     * @param operation
     * @param returnValue
     * @throws ParseException
     */
    @AfterReturning(value = "serviceStatistics(operation)",returning = "returnValue")
    public void doAfterThrowing(DemoApplication.Operation operation,Object returnValue) throws ParseException {
        returnTime = System.currentTimeMillis();
        System.out.println(returnTime);
        Date date = new Date(returnTime);
        /*date.setTime(returnTime);
        System.out.println(new SimpleDateFormat().format(date));*/
        requestLog.setReturnTime(date);
        requestLog.setFinishTime(returnTime);
        //requestLog.setReturnTime(DateUtil.parseDate(String.valueOf(returnTime)));
        //requestLog.setFinishTime(DateUtil.timeDifferLong);
        requestLog.setReturnData(JSON.toJSONString(returnValue, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue));
        //保存到请求日志数据
        requestMapper.insert(requestLog);
    }

    /**
     * 编写方法的异常通知
     * 异常通知，在目标方法非正常结束，发生异常或者抛出异常时执行
     * 在异常通知中设置异常信息，并将其保存
     * @param operation
     * @param e
     */
    @AfterThrowing(value = "serviceStatistics(operation)",throwing = "e")
    public void doAfterThrowing(DemoApplication.Operation operation,Throwable e){
        long happenTime = System.currentTimeMillis();
        Date date = new Date(happenTime);
        exceptionLog.setHappenTime(date);
        exceptionLog.setExceptionJson(JSON.toJSONString(e,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue));
        exceptionLog.setExceptionMessage(e.getMessage());
        exceptionLog.setId(UUID.randomUUID().toString());
        exceptionMapper.insert(exceptionLog);
    }
}
