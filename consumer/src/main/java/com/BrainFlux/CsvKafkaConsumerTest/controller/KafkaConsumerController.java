package com.BrainFlux.CsvKafkaConsumerTest.controller;


import com.BrainFlux.CsvKafkaConsumerTest.common.page.PageResult;
import com.BrainFlux.CsvKafkaConsumerTest.config.InfluxUtil;
import com.BrainFlux.CsvKafkaConsumerTest.config.http.AxiosResult;
import com.BrainFlux.CsvKafkaConsumerTest.controller.base.BaseController;
import com.BrainFlux.CsvKafkaConsumerTest.domain.CsvStreaming;
import com.BrainFlux.CsvKafkaConsumerTest.domain.RowData;
import com.BrainFlux.CsvKafkaConsumerTest.domain.StreamingFailure;
import com.BrainFlux.CsvKafkaConsumerTest.domain.criteria.CsvStreamingQueryCriteria;
import com.BrainFlux.CsvKafkaConsumerTest.domain.criteria.StreamingFailureQueryCriteria;
import com.BrainFlux.CsvKafkaConsumerTest.domain.vo.*;
import com.BrainFlux.CsvKafkaConsumerTest.service.AutoStreaming;
import com.BrainFlux.CsvKafkaConsumerTest.service.ICsvStreamingService;
import com.BrainFlux.CsvKafkaConsumerTest.service.IStreamingFailureService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

/**
 * @Author：geliyang
 * @Version：1.0
 * @Date：2022/3/23-6:10
 * @Since:jdk1.8
 * @Description:TODO
 */

@Api(value = "kafkaConsumer_api", description = "kafkaConsumer_api")
@RequestMapping("/kafkaConsumer_api")
@RestController
public class KafkaConsumerController extends BaseController {
    private final static String dbName = "data";
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static String path="C:\\Users\\Administrator\\Desktop\\kafkaLog\\log.txt";
    private static BatchPoints records = BatchPoints.database(dbName).build();
    private static BatchPoints records2 = BatchPoints.database(dbName).build();
    private static BatchPoints records3 = BatchPoints.database(dbName).build();
    private static Integer batchSize=0;
    private static Integer batchSize2=0;
    private static Integer batchSize3=0;
    private static long totalTime=0;
    private static long totalTime2=0;
    private static long totalTime3=0;
    private static HashMap<String,Integer> checkData=new HashMap<>();
    private static Integer success=0;
    private static Integer failure=0;
    private InfluxDB idb = generateIdbClient();
    private String uuid=UUID.randomUUID().toString().replace("-","");
    private String startTime=" ";
    private ExecutorService executorService=Executors.newFixedThreadPool(3);
    private static Integer threads=2;
    private static  ListeningExecutorService service= MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(threads));
    private static  ListeningExecutorService service2= MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(threads));
    private static  ListeningExecutorService service3= MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(threads));
    private static Integer sleepTime=4000;
    private static boolean checkProcess=false;
    private long finalTotalTime=0;
    private String scheduleTaskID="manually";
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private ICsvStreamingService iCsvStreamingService;
    @Autowired
    private IStreamingFailureService iStreamingFailureService;

    @KafkaListener(groupId = "csv-import-listener-group",topicPartitions = {@TopicPartition(topic ="import-CSV-events",partitions = "0")})
    public void processImportCSVEventOne(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException, ParseException, ExecutionException, InterruptedException {
        RowData rowData = objectMapper.readValue(consumerRecord.value(), RowData.class);
        if(rowData.getTimestamp()!=0&&rowData.getTimestamp()!=1&&rowData.getTimestamp()!=2){
            batchRecord(rowData,records);
            batchSize++;
        }
        if(rowData.getTimestamp()==1){
            moveFileOrErrorReport(rowData,records,sleepTime);
        }
        if(rowData.getTimestamp()==2){
           startTime =rowData.getPid();
        }
        if(batchSize==500){
            writeRecords(service,records);
            records = BatchPoints.database(dbName).build();
            batchSize=0;
        }else if(rowData.getTimestamp()==0){
            if(!rowData.getPid().equals("finish")){
                scheduleTaskID= rowData.getPid();
            }
            wirteMysql(sleepTime,records,totalTime);
        }
    }

    @KafkaListener(groupId = "csv-import-listener-groupTwo",topicPartitions = {@TopicPartition(topic ="import-CSV-events",partitions = "1")})
    public void processImportCSVEventTwo(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException, InterruptedException, ExecutionException, ParseException {
        RowData rowData = objectMapper.readValue(consumerRecord.value(), RowData.class);
        System.out.println(consumerRecord.offset());
        if(rowData.getTimestamp()!=0&&rowData.getTimestamp()!=1&&rowData.getTimestamp()!=2){
            batchRecord(rowData,records2);
            batchSize2++;
        }
        if(rowData.getTimestamp()==1){
            moveFileOrErrorReport(rowData,records2,sleepTime);
        }
        if(rowData.getTimestamp()==2){
            startTime =rowData.getPid();
        }
        if(batchSize2==500){
            writeRecords(service2,records2);
            records2 = BatchPoints.database(dbName).build();
            batchSize2=0;
        }else if(rowData.getTimestamp()==0){
            if(!rowData.getPid().equals("finish")){
                scheduleTaskID= rowData.getPid();
            }
            wirteMysql(sleepTime,records2,totalTime2);
        }
    }

    @KafkaListener(groupId = "csv-import-listener-groupThree",topicPartitions = {@TopicPartition(topic ="import-CSV-events",partitions = "2")})
    public void processImportCSVEventThree(ConsumerRecord<Integer,String> consumerRecord) throws JsonProcessingException, InterruptedException, ExecutionException, ParseException {
        RowData rowData = objectMapper.readValue(consumerRecord.value(), RowData.class);
        System.out.println(consumerRecord.offset());
        if(rowData.getTimestamp()!=0&&rowData.getTimestamp()!=1&&rowData.getTimestamp()!=2){
            batchRecord(rowData,records3);
            batchSize3++;
        }
        if(rowData.getTimestamp()==1){
            moveFileOrErrorReport(rowData,records3,sleepTime);
        }
        if(rowData.getTimestamp()==2){
            startTime =rowData.getPid();
        }
        if(batchSize3==500){
            long start = System.currentTimeMillis();
            writeRecords(service3,records3);
            long end = System.currentTimeMillis();
            records3 = BatchPoints.database(dbName).build();
            batchSize3=0;
        }else if(rowData.getTimestamp()==0){
            if(!rowData.getPid().equals("finish")){
                scheduleTaskID= rowData.getPid();
            }
            wirteMysql(sleepTime,records3,totalTime3);
        }
    }

    private InfluxDB generateIdbClient() {
        // Disable GZip to save CPU
        InfluxDB idb = InfluxUtil.generateIdbClient(true);
        BatchOptions bo = BatchOptions.DEFAULTS.consistency(InfluxDB.ConsistencyLevel.ALL)
                // Flush every 2000 Points, at least every 100ms, buffer for failed oper is 2200
                .actions(500).flushDuration(1000).bufferLimit(10000).jitterDuration(200)
                .exceptionHandler((p, t) -> logger.warn("Write point failed", t));
        idb.enableBatch(bo);
        idb.query(new org.influxdb.dto.Query(String.format("CREATE DATABASE \"%s\"", dbName), dbName));
        return idb;
    }
    private ListenableFuture<String> future= new ListenableFuture<String>() {
        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return false;
        }

        @Override
        public String get() throws InterruptedException, ExecutionException {
            return null;
        }

        @Override
        public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return null;
        }

        @Override
        public void addListener(Runnable listener, Executor executor) {

        }
    };
    @ApiOperation(value = "ConsumerConfig", notes = "ConsumerConfig")
    @PostMapping  ("/ConsumerConfig")
    public AxiosResult<List<ConsumerVo>> consumerConfig(@RequestBody ConsumerVo consumerVo) {
        List<ConsumerVo>list= new ArrayList<>();
        sleepTime=consumerVo.getSleepTime();
        threads= consumerVo.getThreads();
        service= MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(threads));
        service2= MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(threads));
        service3= MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(threads));
        list.add(consumerVo);
        return AxiosResult.success(list);
    }
    @ApiOperation(value = "searchConsumerConfig", notes = "searchConsumerConfig")
    @PostMapping  ("/searchConsumerConfig")
    public AxiosResult<ServerInfoVo> searchConsumerConfig() {
        ServerInfoVo serverInfoVo=new ServerInfoVo();
        serverInfoVo.setSleepTime(sleepTime);
        serverInfoVo.setThread(threads);
        serverInfoVo.setServer("csvToInfluxDB");
        serverInfoVo.setType("consumer");
        serverInfoVo.setLocation("pittsburgh");
        serverInfoVo.setStatus("running");
        return AxiosResult.success(serverInfoVo);
    }
    @ApiOperation(value = "checkProcess", notes = "checkProcess")
    @GetMapping("/checkProcess")
    public AxiosResult<ResultVo> finishOrNot() {
        ResultVo resultVo=new ResultVo();
        resultVo.setFailure(failure);
        resultVo.setSuccess(success);
        resultVo.setTotal(failure+success);
        resultVo.setTotalTime(finalTotalTime);
        resultVo.setProcess(checkProcess);
        return AxiosResult.success(resultVo);
    }
    @ApiOperation(value = "checkCsvStreamingByQuery", notes = "checkCsvStreamingByQuery")
    @PostMapping("/checkAllCsvStreamingByQuery")
    public AxiosResult<PageResult<CsvStreamingVo>> checkCsvStreamingByQuery(@RequestBody CsvStreamingQueryCriteria csvStreamingQueryCriteria) {
        PageResult<CsvStreamingVo>list=iCsvStreamingService.checkAllCsvStreamingByQuery(csvStreamingQueryCriteria);
        return AxiosResult.success(list);
    }
    @ApiOperation(value = "checkStreamingFailureByEid", notes = "checkStreamingFailureByEid")
    @PostMapping("/checkStreamingFailureByEid")
    public AxiosResult<List<StreamingFailureVo>> checkCsvStreamingByQuery(@RequestBody StreamingFailureQueryCriteria streamingFailureQueryCriteria) {
        List<StreamingFailureVo>list=iStreamingFailureService.checkStreamingFailureByEid(streamingFailureQueryCriteria);
        return AxiosResult.success(list);
    }
    private void batchRecord(RowData rowData,BatchPoints records){
        Map<String, Object> rowValues=rowData.getRowValues();
        long measurementEpoch = rowData.getTimestamp();
        String pid= rowData.getPid();
        Point record = Point.measurement(pid).time(measurementEpoch, TimeUnit.MILLISECONDS).fields(rowValues).build();
        records.point(record);
        if(!checkData.containsKey(pid)){
            checkData.put(pid,1);
        }else {
            int count=checkData.get(pid);
            count+=1;
            checkData.put(pid,count);
        }
    }
    private void writeRecords(ListeningExecutorService service,final BatchPoints records) throws InterruptedException {
        future= (ListenableFuture<String>) service.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                idb.write(records);
                return Thread.currentThread().getName();
            }
        });
        Futures.addCallback(future, new FutureCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("task:" +result);
            };

            @Override
            public void onFailure(Throwable t) {
            }
        });
        Thread.sleep(sleepTime);
    }
    private void wirteMysql(Integer sleepTime,BatchPoints records,long totalTimeFinal) throws ParseException, InterruptedException, ExecutionException {
        future.get();
        Thread.sleep(sleepTime);
        long start = System.currentTimeMillis();
        idb.write(records);
        long end = System.currentTimeMillis();
        idb.close();

        CsvStreaming csvStreaming=new CsvStreaming();
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateFormat tmp=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDateTime=localDateTime.format(formatter);
        //duration time
        Date startTimeValue= tmp.parse(startTime);
        Date endTimeValue= tmp.parse(formatDateTime);
        csvStreaming.setStartTime(startTime);
        finalTotalTime=(endTimeValue.getTime()-startTimeValue.getTime())/(1000*60);
        System.out.println((endTimeValue.getTime()-startTimeValue.getTime())/1000+" s");
        if(startTime.equals(" ")){
            csvStreaming.setStartTime("startTimeLose");
            csvStreaming.setUsedTime("startTimeLose");
        }else {
            long minutes=(endTimeValue.getTime()-startTimeValue.getTime())/(1000*60);
            csvStreaming.setUsedTime(String.valueOf(minutes));
        }
        Thread.sleep(sleepTime*10);
        csvStreaming.setEid(uuid);
        csvStreaming.setFailure(failure);
        csvStreaming.setSuccess(success);
        csvStreaming.setAmount(failure+success);
        csvStreaming.setEndTime(formatDateTime);
        csvStreaming.setConsumergroupNumber(3);
        csvStreaming.setProducerAddress("pittsburgh");
        csvStreaming.setConsumerAddress("pittsburgh");
        csvStreaming.setThreadNumber(threads);
        csvStreaming.setSleepTime(sleepTime);
        csvStreaming.setTopicName("csvToInfluxDB");
        csvStreaming.setScheduleTask(scheduleTaskID);
        csvStreaming.setAdditionOne("one");
        csvStreaming.setAdditionTwo("two");
        csvStreaming.setAdditionThree("three");
        csvStreaming.setAdditionFour("Four");
        try {
            iCsvStreamingService.insert(csvStreaming);
        }catch (Exception e){

        }
        checkProcess=true;
    }

    private void moveFileOrErrorReport(RowData rowData,BatchPoints records,Integer sleepTime) throws InterruptedException {
        idb.write(records);
        Thread.sleep(sleepTime);
        Map<String, Object> rowValues=rowData.getRowValues();
        int number= (int) rowValues.get("finishThisFile");
        String filePath= (String) rowValues.get("filePath");
        File fileTmp= new File(filePath);
        AutoStreaming autoStreaming=new AutoStreaming();
        String pid= rowData.getPid();
        String fileName=filePath.split("\\\\")[5].split("_")[0];
        System.out.println(fileName);
        //double check
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("SELECT count(Time) FROM \"");
        stringBuffer.append(fileName);
        stringBuffer.append("\"");
        String command=stringBuffer.toString();
        QueryResult queryResult= idb.query(new org.influxdb.dto.Query(command,"data"));
        double resCount= (double) queryResult.getResults().get(0).getSeries().get(0).getValues().get(0).get(1);
        System.out.println(resCount);
        if(number==checkData.get(pid)&&resCount==checkData.get(pid)){
            try {
                String path2="C:\\Users\\Administrator\\Desktop\\kafkaTest";
                System.out.println(filePath);
                autoStreaming.moveFile(path2,fileTmp);
                success++;
                System.out.println(success);
            }catch (Exception e){
                autoStreaming.setLog2("moveFailure",path);
            }
        }else if(number!=checkData.get(pid)) {
            StringBuffer str=new StringBuffer();
            StreamingFailure streamingFailure=new StreamingFailure();
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String [] tmp=filePath.split("\\\\");
            String []res=tmp[5].split("_");
            String formatDateTime=localDateTime.format(formatter);
            streamingFailure.setEndTime(formatDateTime);
            streamingFailure.setEid(uuid);
            streamingFailure.setFid(res[0]);
            streamingFailure.setTotalData(number);
            streamingFailure.setDetail("lose "+(number-checkData.get(pid))+" row Data");
            iStreamingFailureService.insert(streamingFailure);
            str.append("loseData: ");
            str.append(filePath);
            autoStreaming.setLog2(str.toString(),path);
            failure++;
        }else if(number==checkData.get(pid)&&resCount!=checkData.get(pid)){
            StringBuffer str=new StringBuffer();
            StreamingFailure streamingFailure=new StreamingFailure();
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String [] tmp=filePath.split("\\\\");
            String []res=tmp[5].split("_");
            String formatDateTime=localDateTime.format(formatter);
            streamingFailure.setEndTime(formatDateTime);
            streamingFailure.setEid(uuid);
            streamingFailure.setFid(res[0]);
            streamingFailure.setTotalData(checkData.get(pid));
            streamingFailure.setDetail("writeFailure "+(checkData.get(pid)-resCount)+" row Data");
            iStreamingFailureService.insert(streamingFailure);
            str.append("writeFailure "+(checkData.get(pid)-resCount)+" row Data");
            str.append(filePath);
            autoStreaming.setLog2(str.toString(),path);
            failure++;
        }
    }

}
