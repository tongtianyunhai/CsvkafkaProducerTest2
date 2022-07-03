package com.BrainFlux.CsvKafkaConsumerTest.domain.criteria;

import com.BrainFlux.CsvKafkaConsumerTest.domain.criteria.base.BaseQueryCriteria;
import lombok.Data;

/**
 * @Author：geliyang
 * @Version：1.0
 * @Date：2022/6/13-17:49
 * @Since:jdk1.8
 * @Description:TODO
 */
@Data
public class StreamingFailureQueryCriteria extends BaseQueryCriteria {
    private  String eid;
}
