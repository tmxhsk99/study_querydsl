package study.querydsl.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeviceInfo {
    @Id
    @Column(name = "device_code", columnDefinition = "VARCHAR(200)")
    private Integer deviceCode;
    @Column(name = "name", columnDefinition = "VARCHAR(200)")
    private String name;
    @Builder
    public DeviceInfo(Integer deviceCode, String name) {
        this.deviceCode = deviceCode;
        this.name = name;
    }
}
