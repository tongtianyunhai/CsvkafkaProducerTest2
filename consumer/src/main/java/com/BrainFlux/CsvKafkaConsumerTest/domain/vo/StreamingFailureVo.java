package com.BrainFlux.CsvKafkaConsumerTest.domain.vo;

import lombok.Data;

/**
 * @Author：geliyang
 * @Version：1.0
 * @Date：2022/6/13-17:43
 * @Since:jdk1.8
 * @Description:TODO
 */
@Data
public class StreamingFailureVo {

    private static final long serialVersionUID = 1L;

    private String fid;

    private String endTime;

    private Integer totalData;

    private String detail;

    private String eid;

}
