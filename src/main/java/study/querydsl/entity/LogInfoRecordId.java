package study.querydsl.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
public class LogInfoRecordId implements Serializable {
    private String oid; // FaultRecord.pvid 매핑

    private Integer faultCode;

    private Integer deviceCode;

    @Builder
    public LogInfoRecordId(String oid, Integer faultCode, Integer deviceCode) {
        this.oid = oid;
        this.faultCode = faultCode;
        this.deviceCode = deviceCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(oid,faultCode,deviceCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null){
            return false;
        }
        if(getClass() != obj.getClass()){
            return false;
        }
        LogInfoRecordId input = (LogInfoRecordId) obj;

        return input.oid.equals(this.oid) && input.faultCode == this.faultCode && input.deviceCode == this.deviceCode;
    }

}
