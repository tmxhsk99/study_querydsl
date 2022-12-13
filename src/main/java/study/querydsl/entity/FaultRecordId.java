package study.querydsl.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class FaultRecordId implements Serializable {
    private String pvid; // FaultRecord.pvid 매핑

    private Integer faultCode;

    private Integer deviceCode;


    @Override
    public int hashCode() {
        return Objects.hash(pvid)+Objects.hash(faultCode)+Objects.hash(deviceCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj))
            return false;
        FaultRecordId input = (FaultRecordId) obj;
        if(input.pvid.equals(this.pvid) && input.faultCode == this.faultCode && input.deviceCode == this.deviceCode){
            return true;
        }
        return false;
    }

}
