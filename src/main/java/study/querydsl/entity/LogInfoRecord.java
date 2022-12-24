package study.querydsl.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@IdClass(LogInfoRecordId.class)
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class LogInfoRecord {
    @Id
    private String oid;

    @Id
    private Integer faultCode;

    @Id
    private Integer deviceCode;

    private Integer speed;

    private Integer faultGroup;

    @CreationTimestamp
    private LocalDateTime occurTime;

    private String occurDate;

    @Builder
    public LogInfoRecord(String oid, Integer faultCode, Integer deviceCode, Integer speed, Integer faultGroup, LocalDateTime occurTime, String occurDate) {
        this.oid = oid;
        this.faultCode = faultCode;
        this.deviceCode = deviceCode;
        this.speed = speed;
        this.faultGroup = faultGroup;
        this.occurTime = occurTime;
        this.occurDate = occurDate;
    }
}
