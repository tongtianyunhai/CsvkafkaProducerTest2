package com.BrainFlux.CsvKafkaConsumerTest.domain.vo;

import lombok.Data;

/**
 * @Author：geliyang
 * @Version：1.0
 * @Date：2022/5/28-21:49
 * @Since:jdk1.8
 * @Description:TODO
 */
@Data
public class ResultVo {
    private Integer success;
    private Integer failure;
    private Integer total;
    private long totalTime;
    private Boolean process;
}
