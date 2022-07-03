package com.BrainFlux.CsvKafkaConsumerTest.domain.vo;

import lombok.Data;

/**
 * @Author：geliyang
 * @Version：1.0
 * @Date：2022/5/24-23:38
 * @Since:jdk1.8
 * @Description:TODO
 */
@Data
public class ConsumerVo {
    private Integer threads;
    private Integer sleepTime;
}
