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
    private Integer deviceCode;
    private String name;
    @Builder
    public DeviceInfo(Integer deviceCode, String name) {
        this.deviceCode = deviceCode;
        this.name = name;
    }
}
