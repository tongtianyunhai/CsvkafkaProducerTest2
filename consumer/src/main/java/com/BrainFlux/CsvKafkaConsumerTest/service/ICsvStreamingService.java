package com.BrainFlux.CsvKafkaConsumerTest.service;

import com.BrainFlux.CsvKafkaConsumerTest.domain.CsvStreaming;
import com.BrainFlux.CsvKafkaConsumerTest.domain.base.BaseQueryCriteria;
import com.BrainFlux.CsvKafkaConsumerTest.service.Base.BaseService;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author whl
 * @since 2022-04-27
 */
public interface ICsvStreamingService extends BaseService<CsvStreaming> {
    CsvStreaming selectByTime(BaseQueryCriteria baseQueryCriteria);
}
