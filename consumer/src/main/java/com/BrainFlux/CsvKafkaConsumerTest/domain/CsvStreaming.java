package com.BrainFlux.CsvKafkaConsumerTest.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author：geliyang
 * @Version：1.0
 * @Date：2022/4/26-15:05
 * @Since:jdk1.8
 * @Description:TODO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CsvStreaming implements Serializable {
    private static final long serialVersionUID = 1L;
    private String eid;
    private String time;
    private Integer success;
    private Integer failure;
    private Integer amount;
}
