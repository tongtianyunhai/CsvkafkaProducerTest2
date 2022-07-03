package com.BrainFlux.CsvKafkaConsumerTest.service.impl;

import com.BrainFlux.CsvKafkaConsumerTest.domain.CsvStreaming;
import com.BrainFlux.CsvKafkaConsumerTest.domain.StreamingFailure;
import com.BrainFlux.CsvKafkaConsumerTest.domain.criteria.StreamingFailureQueryCriteria;
import com.BrainFlux.CsvKafkaConsumerTest.domain.vo.StreamingFailureVo;
import com.BrainFlux.CsvKafkaConsumerTest.mapper.CsvStreamingMapper;
import com.BrainFlux.CsvKafkaConsumerTest.mapper.StreamingFailureMapper;
import com.BrainFlux.CsvKafkaConsumerTest.service.Base.impl.BaseServiceImpl;
import com.BrainFlux.CsvKafkaConsumerTest.service.ICsvStreamingService;
import com.BrainFlux.CsvKafkaConsumerTest.service.IStreamingFailureService;
import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author whl
 * @since 2022-05-04
 */
@Service
@Transactional
@RequiredArgsConstructor
public class StreamingFailureServiceImpl extends BaseServiceImpl<StreamingFailure> implements IStreamingFailureService {
    @Resource
    private StreamingFailureMapper streamingFailureMapper;

    @Override
    public StreamingFailure insert(StreamingFailure streamingFailure) {
        int res= streamingFailureMapper.insert(streamingFailure);
        System.out.println(res+"res");
        return streamingFailure;
    }

    @Override
    public List<StreamingFailureVo> checkStreamingFailureByEid(StreamingFailureQueryCriteria streamingFailureQueryCriteria) {
        return streamingFailureMapper.checkStreamingFailureByEid(streamingFailureQueryCriteria);
    }
}
