package study.querydsl.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FaultRecordViewResponse {
    private String pvid;
    private String faultCode;
    private String faultCodeMessage;
    private Integer deviceCode;
    private String deviceInfoName;
    private LocalDateTime occurTime;
    private Integer speed;
    private Integer faultGubun;
    private Integer car_no;
    private Integer direct;
}
