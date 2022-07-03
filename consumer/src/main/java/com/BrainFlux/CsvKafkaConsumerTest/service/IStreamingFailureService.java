package com.BrainFlux.CsvKafkaConsumerTest.service;

import com.BrainFlux.CsvKafkaConsumerTest.domain.CsvStreaming;
import com.BrainFlux.CsvKafkaConsumerTest.domain.StreamingFailure;
import com.BrainFlux.CsvKafkaConsumerTest.domain.criteria.StreamingFailureQueryCriteria;
import com.BrainFlux.CsvKafkaConsumerTest.domain.vo.StreamingFailureVo;
import com.BrainFlux.CsvKafkaConsumerTest.service.Base.BaseService;
import java.util.*;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author whl
 * @since 2022-05-04
 */
public interface IStreamingFailureService extends BaseService<StreamingFailure> {
    StreamingFailure insert(StreamingFailure streamingFailure);
    List<StreamingFailureVo> checkStreamingFailureByEid(StreamingFailureQueryCriteria streamingFailureQueryCriteria);
}
