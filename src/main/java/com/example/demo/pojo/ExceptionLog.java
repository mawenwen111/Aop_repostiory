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
@TableName("exception_log")
public class ExceptionLog {
    @TableId(value = "id")
    private String id;
    private String exceptionJson;
    private String exceptionMessage;
    @DateTimeFormat
    private Date happenTime;
}
