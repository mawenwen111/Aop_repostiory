package com.example.demo.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("request_log")
public class RequestLog {
    @TableId(value = "id")
    private String id;
    private String ip;
    private String url;
    private Integer type;
    private String way;
    private String classPath;
    private String methodName;
    private String param;
    private String operation;
    private String sessionId;
    @DateTimeFormat
    private Date startTime;
    private Long finishTime;
    @DateTimeFormat
    private Date returnTime;
    private String returnData;
}
