package com.BrainFlux.CsvKafkaConsumerTest.domain.criteria.base;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class BaseQueryCriteria {
    private Integer currentPage=1;//当前所在页

    private Integer pageSize=5;//当前页显示

    //开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    //结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
