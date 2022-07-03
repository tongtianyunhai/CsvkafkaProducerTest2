package com.BrainFlux.CsvKafkaConsumerTest.mapper;

import com.BrainFlux.CsvKafkaConsumerTest.domain.CsvStreaming;
import com.BrainFlux.CsvKafkaConsumerTest.domain.StreamingFailure;
import com.BrainFlux.CsvKafkaConsumerTest.domain.criteria.StreamingFailureQueryCriteria;
import com.BrainFlux.CsvKafkaConsumerTest.domain.vo.StreamingFailureVo;
import com.BrainFlux.CsvKafkaConsumerTest.mapper.base.MyMapper;
import org.springframework.stereotype.Repository;
import java.util.*;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author whl
 * @since 2022-05-04
 */
@Repository
public interface StreamingFailureMapper extends MyMapper<StreamingFailure> {
    int insert(StreamingFailure streamingFailure);
    List<StreamingFailureVo> checkStreamingFailureByEid(StreamingFailureQueryCriteria streamingFailureQueryCriteria);
}
