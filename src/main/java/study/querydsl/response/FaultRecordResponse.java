package study.querydsl.response;

import com.querydsl.core.Tuple;
import lombok.Data;
import study.querydsl.entity.DeviceInfo;
import study.querydsl.entity.FaultCode;
import study.querydsl.entity.LogInfoRecord;

import java.time.LocalDateTime;

@Data
public class FaultRecordResponse {
    private String oid;
    private String faultCode;
    private String faultCodeMessage;
    private Integer deviceCode;
    private String deviceInfoName;
    private Integer faultGroup;
    private String faultMessage;
    private LocalDateTime occurTime;
    private String deviceName;
    private Integer speed;

    public FaultRecordResponse(Tuple tuple) {
        LogInfoRecord logInfoRecord = tuple.get(0, LogInfoRecord.class);
        FaultCode faultCode = tuple.get(1, FaultCode.class);
        DeviceInfo deviceInfo = tuple.get(2, DeviceInfo.class);

        this.oid = logInfoRecord.getOid();
        this.faultCode = faultCode.getFaultCode();
        this.deviceCode = logInfoRecord.getFaultCode();
        this.faultGroup = logInfoRecord.getFaultGroup();
        this.faultMessage = faultCode.getMessage();
        this.deviceName = deviceInfo.getName();
        this.occurTime = logInfoRecord.getOccurTime();
        this.speed = logInfoRecord.getSpeed();

    }
}
