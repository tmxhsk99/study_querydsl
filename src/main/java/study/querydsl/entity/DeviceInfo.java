package study.querydsl.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class DeviceInfo {
    @Id
    @Column(name = "device_code", columnDefinition = "INT(11)")
    private Integer deviceCode;
    private String name;
    @Builder
    public DeviceInfo(Integer deviceCode, String name) {
        this.deviceCode = deviceCode;
        this.name = name;
    }
}
