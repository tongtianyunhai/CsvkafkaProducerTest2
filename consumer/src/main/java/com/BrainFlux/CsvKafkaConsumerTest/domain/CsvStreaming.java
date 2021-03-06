package com.BrainFlux.CsvKafkaConsumerTest.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author：geliyang
 * @Version：1.0
 * @Date：2022/4/26-15:05
 * @Since:jdk1.8
 * @Description:TODO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CsvStreaming  implements Serializable {
    private static final long serialVersionUID = 1L;
    private String eid;

    private String usedTime;

    private Integer success;

    private Integer failure;

    private Integer amount;

    private String startTime;

    private String endTime;

    private Integer threadNumber;

    private Integer sleepTime;

    private Integer consumergroupNumber;

    private String producerAddress;

    private String consumerAddress;

    private String topicName;

    private String scheduleTask;

    private String additionOne;

    private String additionTwo;

    private String additionThree;

    private String additionFour;
}
