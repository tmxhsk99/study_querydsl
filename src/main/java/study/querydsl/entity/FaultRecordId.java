package study.querydsl.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
public class FaultRecordId implements Serializable {
    private String pvid; // FaultRecord.pvid 매핑

    private Integer faultCode;

    private Integer deviceCode;

    @Builder
    public FaultRecordId(String pvid, Integer faultCode, Integer deviceCode) {
        this.pvid = pvid;
        this.faultCode = faultCode;
        this.deviceCode = deviceCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pvid,faultCode,deviceCode);
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
        FaultRecordId input = (FaultRecordId) obj;

        return input.pvid.equals(this.pvid) && input.faultCode == this.faultCode && input.deviceCode == this.deviceCode;
    }

}
