package com.BrainFlux.CsvKafkaConsumerTest.domain.vo;

import lombok.Data;

/**
 * @Author：geliyang
 * @Version：1.0
 * @Date：2022/6/13-16:19
 * @Since:jdk1.8
 * @Description:TODO
 */
@Data
public class CsvStreamingVo {
    private String eid;

    private String usedTime;

    private Integer success;

    private Integer failure;

    private Integer amount;

    private String startTime;

    private String endTime;

    private Integer threadNumber;

    private Integer sleepTime;

    private Integer consumergroupNumber;

    private String producerAddress;

    private String consumerAddress;

    private String topicName;

    private String scheduleTask;

}
