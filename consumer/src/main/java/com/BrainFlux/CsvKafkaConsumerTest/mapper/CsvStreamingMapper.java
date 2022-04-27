package com.BrainFlux.CsvKafkaConsumerTest.mapper;

import com.BrainFlux.CsvKafkaConsumerTest.domain.CsvStreaming;
import com.BrainFlux.CsvKafkaConsumerTest.domain.base.BaseQueryCriteria;
import com.BrainFlux.CsvKafkaConsumerTest.mapper.base.MyMapper;
import org.springframework.stereotype.Repository;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author whl
 * @since 2022-04-27
 */
@Repository
public interface CsvStreamingMapper extends MyMapper<CsvStreaming> {
    CsvStreaming selectByTime(BaseQueryCriteria baseQueryCriteria);
}
