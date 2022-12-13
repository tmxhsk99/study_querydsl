package study.querydsl.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@IdClass(FaultRecordId.class)
@NoArgsConstructor
@ToString
public class FaultRecord {
    @Id
    private String pvid;

    @Id
    private Integer faultCode;

    @Id
    private Integer deviceCode;

    private Integer speed;


    private Integer faultGroup;

    @Builder
    public FaultRecord(String pvid, Integer faultCode, Integer deviceCode, Integer speed, Integer faultGroup) {
        this.pvid = pvid;
        this.faultCode = faultCode;
        this.deviceCode = deviceCode;
        this.speed = speed;
        this.faultGroup = faultGroup;
    }
}
