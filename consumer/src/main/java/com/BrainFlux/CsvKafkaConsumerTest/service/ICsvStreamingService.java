package com.BrainFlux.CsvKafkaConsumerTest.service;

import com.BrainFlux.CsvKafkaConsumerTest.common.page.PageResult;
import com.BrainFlux.CsvKafkaConsumerTest.domain.CsvStreaming;
import com.BrainFlux.CsvKafkaConsumerTest.domain.criteria.CsvStreamingQueryCriteria;
import com.BrainFlux.CsvKafkaConsumerTest.domain.vo.CsvStreamingVo;
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
    CsvStreaming insert(CsvStreaming csvStreaming);
    PageResult<CsvStreamingVo> checkAllCsvStreamingByQuery(CsvStreamingQueryCriteria csvStreamingQueryCriteria);

}
