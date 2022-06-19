package com.BrainFlux.CsvKafkaConsumerTest.service.impl;

import com.BrainFlux.CsvKafkaConsumerTest.common.page.PageResult;
import com.BrainFlux.CsvKafkaConsumerTest.domain.CsvStreaming;
import com.BrainFlux.CsvKafkaConsumerTest.domain.criteria.CsvStreamingQueryCriteria;
import com.BrainFlux.CsvKafkaConsumerTest.domain.vo.CsvStreamingVo;
import com.BrainFlux.CsvKafkaConsumerTest.mapper.CsvStreamingMapper;
import com.BrainFlux.CsvKafkaConsumerTest.service.Base.impl.BaseServiceImpl;
import com.BrainFlux.CsvKafkaConsumerTest.service.ICsvStreamingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

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
    @Resource
    private  CsvStreamingMapper csvStreamingMapper;

    @Override
    public CsvStreaming insert(CsvStreaming csvStreaming) {
        int res= csvStreamingMapper.insert(csvStreaming);
        System.out.println(res+"res");
        return csvStreaming;
    }

    @Override
    public PageResult<CsvStreamingVo> checkAllCsvStreamingByQuery(CsvStreamingQueryCriteria csvStreamingQueryCriteria) {
        Integer currentPage = csvStreamingQueryCriteria.getCurrentPage();
        Integer pageSize = csvStreamingQueryCriteria.getPageSize();

        if (StringUtils.isEmpty(currentPage)) {
            currentPage = 1;
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = 5;
        }

        PageHelper.startPage(currentPage, pageSize);

        List<CsvStreamingVo> csvStreamingVoList = csvStreamingMapper.checkCsvStreamingByQuery(csvStreamingQueryCriteria);

        PageInfo<CsvStreamingVo> scheduleTaskPageInfo = new PageInfo<>(csvStreamingVoList);

        long total = scheduleTaskPageInfo.getTotal();

        return new PageResult<CsvStreamingVo>(total, csvStreamingVoList);
    }
}
