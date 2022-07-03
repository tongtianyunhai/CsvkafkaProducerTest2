package com.BrainFlux.CsvKafkaConsumerTest.domain.criteria;


import com.BrainFlux.CsvKafkaConsumerTest.domain.criteria.base.BaseQueryCriteria;
import lombok.Data;

/**
 * @Author：geliyang
 * @Version：1.0
 * @Date：2022/6/13-16:40
 * @Since:jdk1.8
 * @Description:TODO
 */
@Data
public class CsvStreamingQueryCriteria extends BaseQueryCriteria {
    private String scheduleTask;

    private String producerAddress;

    private String consumerAddress;

    private String topicName;
}
