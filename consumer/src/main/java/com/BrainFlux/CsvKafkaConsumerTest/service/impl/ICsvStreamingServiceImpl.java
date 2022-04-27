package com.BrainFlux.CsvKafkaConsumerTest.service.impl;

import com.BrainFlux.CsvKafkaConsumerTest.domain.CsvStreaming;
import com.BrainFlux.CsvKafkaConsumerTest.domain.base.BaseQueryCriteria;
import com.BrainFlux.CsvKafkaConsumerTest.mapper.CsvStreamingMapper;
import com.BrainFlux.CsvKafkaConsumerTest.service.Base.impl.BaseServiceImpl;
import com.BrainFlux.CsvKafkaConsumerTest.service.ICsvStreamingService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author whl
 * @since 2022-04-27
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ICsvStreamingServiceImpl extends BaseServiceImpl<CsvStreaming> implements ICsvStreamingService{
    private final CsvStreamingMapper csvStreamingMapper;

    @Override
    public CsvStreaming selectByTime(BaseQueryCriteria baseQueryCriteria) {
        return csvStreamingMapper.selectByTime(baseQueryCriteria);
    }
}
