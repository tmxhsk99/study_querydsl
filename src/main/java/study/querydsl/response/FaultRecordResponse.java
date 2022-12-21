package study.querydsl.response;

import com.querydsl.core.Tuple;
import lombok.Data;
import study.querydsl.entity.DeviceInfo;
import study.querydsl.entity.FaultCode;
import study.querydsl.entity.FaultRecord;

import java.time.LocalDateTime;

@Data
public class FaultRecordResponse {
    private String pvid;
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
        FaultRecord faultRecord = tuple.get(0, FaultRecord.class);
        FaultCode faultCode = tuple.get(1, FaultCode.class);
        DeviceInfo deviceInfo = tuple.get(2, DeviceInfo.class);

        this.pvid = faultRecord.getPvid();
        this.faultCode = faultCode.getFaultCode();
        this.deviceCode = faultRecord.getFaultCode();
        this.faultGroup = faultRecord.getFaultGroup();
        this.faultMessage = faultCode.getMessage();
        this.deviceName = deviceInfo.getName();
        this.occurTime = faultRecord.getOccurTime();
        this.speed = faultRecord.getSpeed();

    }
}
