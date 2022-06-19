package com.BrainFlux.CsvKafkaConsumerTest.mapper;

import com.BrainFlux.CsvKafkaConsumerTest.domain.CsvStreaming;
import com.BrainFlux.CsvKafkaConsumerTest.domain.criteria.CsvStreamingQueryCriteria;
import com.BrainFlux.CsvKafkaConsumerTest.domain.vo.CsvStreamingVo;
import com.BrainFlux.CsvKafkaConsumerTest.mapper.base.MyMapper;
import org.springframework.stereotype.Repository;

import java.util.List;


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
    List<CsvStreamingVo> checkCsvStreamingByQuery(CsvStreamingQueryCriteria csvStreamingQueryCriteria);
    int insert(CsvStreaming csvStreaming);
}
