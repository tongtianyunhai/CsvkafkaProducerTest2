package com.BrainFlux.CsvKafkaConsumerTest;

import com.BrainFlux.CsvKafkaConsumerTest.domain.CsvStreaming;
import com.BrainFlux.CsvKafkaConsumerTest.domain.base.BaseQueryCriteria;
import com.BrainFlux.CsvKafkaConsumerTest.service.ICsvStreamingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@SpringBootTest (classes =KafkaProducerApplication.class)
public class KafkaProducerApplicationTests {
    @Autowired
    ICsvStreamingService iCsvStreamingService;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    //结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Test
    public void addDocument()  {
        CsvStreaming csvStreaming=new CsvStreaming();
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        String formatDateTime=localDateTime.format(formatter);
        String uuid= UUID.randomUUID().toString().replace("-","");
        csvStreaming.setEid(uuid);
        csvStreaming.setFailure(1);
        csvStreaming.setSuccess(1);
        csvStreaming.setAmount(2);
        csvStreaming.setTime(formatDateTime);
        BaseQueryCriteria baseQueryCriteria=new BaseQueryCriteria();
        baseQueryCriteria.setStartTime(localDateTime);
        baseQueryCriteria.setEndTime(localDateTime);
        try{
            iCsvStreamingService.save(csvStreaming);
            System.out.println(iCsvStreamingService.selectByTime(baseQueryCriteria));
        }catch (NullPointerException e){
            System.out.println("ok");
        }
    }
}
