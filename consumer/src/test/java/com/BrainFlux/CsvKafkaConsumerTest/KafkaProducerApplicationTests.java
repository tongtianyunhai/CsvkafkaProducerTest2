package com.BrainFlux.CsvKafkaConsumerTest;

import com.BrainFlux.CsvKafkaConsumerTest.domain.CsvStreaming;
import com.BrainFlux.CsvKafkaConsumerTest.domain.StreamingFailure;
import com.BrainFlux.CsvKafkaConsumerTest.service.ICsvStreamingService;
import com.BrainFlux.CsvKafkaConsumerTest.service.IStreamingFailureService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@SpringBootTest (classes =KafkaProducerApplication.class)
public class KafkaProducerApplicationTests {
    @Autowired
    ICsvStreamingService iCsvStreamingService;
    @Autowired
    IStreamingFailureService iStreamingFailureService;
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime startTime;
//    //结束时间
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime endTime;
//    private InfluxDB idb = generateIdbClient();
//    private final static String dbName = "data";
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Test
    public void addDocument() throws ParseException {
        CsvStreaming csvStreaming=new CsvStreaming();
        StreamingFailure streamingFailure=new StreamingFailure();
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateFormat tmp=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
        String formatDateTime=localDateTime.format(formatter);
        String uuid= UUID.randomUUID().toString().replace("-","");
        csvStreaming.setEid(uuid);
        csvStreaming.setFailure(1);
        csvStreaming.setSuccess(1);
        csvStreaming.setAmount(2);
        String startTime= localDateTime.format(formatter);
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        LocalDateTime localDateTime1=LocalDateTime.now();
        String endTime=localDateTime1.format(formatter);

        csvStreaming.setStartTime(startTime);
        Date startTimeValue= tmp.parse(startTime);
        Date endTimeValue= tmp.parse(endTime);
        long minutes=(endTimeValue.getTime()-startTimeValue.getTime())/(1000*60);

        csvStreaming.setUsedTime(String.valueOf(minutes));
        csvStreaming.setStartTime(startTime);
        csvStreaming.setEndTime(endTime);
        csvStreaming.setThreadNumber(2);
        csvStreaming.setSleepTime(3000);
        csvStreaming.setConsumerAddress("pitt");
        csvStreaming.setProducerAddress("pitt");
        csvStreaming.setScheduleTask("manually");
        csvStreaming.setAdditionFour("pitt");
        csvStreaming.setAdditionTwo("pitt");
        csvStreaming.setAdditionOne("pitt");
        csvStreaming.setAdditionThree("pitt");
        csvStreaming.setConsumergroupNumber(3);
        csvStreaming.setTopicName("csvToInfluxDB");
        iCsvStreamingService.insert(csvStreaming);
//        streamingFailure.setDetail("loseDate");
//        streamingFailure.setFid("PHU2015");
//        streamingFailure.setEid(uuid);
//        streamingFailure.setEndTime(endTime);
//        streamingFailure.setTotalData(1500);
//        streamingFailure.setAdditionOne("pitt");
//        streamingFailure.setAdditionTwo("pitt");
//        streamingFailure.setAdditionThree("pitt");
//        streamingFailure.setAdditionFour("pitt");
//        iStreamingFailureService.insert(streamingFailure);





//        BaseQueryCriteria baseQueryCriteria=new BaseQueryCriteria();
//        baseQueryCriteria.setStartTime(localDateTime);
//        baseQueryCriteria.setEndTime(localDateTime);
//        try{
//            iCsvStreamingService.save(csvStreaming);
//            System.out.println(iCsvStreamingService.selectByTime(baseQueryCriteria));
//        }catch (NullPointerException e){
//            System.out.println("ok");
//        }
//        StringBuffer stringBuffer=new StringBuffer();
//        stringBuffer.append("SELECT count(Time) FROM \"");
//        String tmp="PUH-2015-0152";
//        stringBuffer.append(tmp);
//        stringBuffer.append("\"");
//        String command=stringBuffer.toString();
//        QueryResult queryResult= idb.query(new org.influxdb.dto.Query(command,"data"));
//        System.out.println(queryResult.getResults().get(0).getSeries().get(0).getValues().get(0).get(1));




    }


//    private InfluxDB generateIdbClient() {
//        // Disable GZip to save CPU
//        InfluxDB idb = InfluxUtil.generateIdbClient(true);
//        BatchOptions bo = BatchOptions.DEFAULTS.consistency(InfluxDB.ConsistencyLevel.ALL)
//                // Flush every 2000 Points, at least every 100ms, buffer for failed oper is 2200
//                .actions(100).flushDuration(1000).bufferLimit(10000).jitterDuration(200)
//                .exceptionHandler((p, t) -> logger.warn("Write point failed", t));
//        idb.enableBatch(bo);
//        idb.query(new org.influxdb.dto.Query(String.format("CREATE DATABASE \"%s\"", dbName), dbName));
//
//        return idb;
//    }
}
