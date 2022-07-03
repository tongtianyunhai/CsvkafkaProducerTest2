package com.BrainFlux.CsvKafkaConsumerTest.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author whl
 * @since 2022-05-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StreamingFailure implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fid;

    private String endTime;

    private Integer totalData;

    private String detail;

    private String eid;


    private String additionOne;

    private String additionTwo;

    private String additionThree;

    private String additionFour;
}
