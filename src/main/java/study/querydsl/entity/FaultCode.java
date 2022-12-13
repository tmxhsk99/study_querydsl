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
public class FaultCode {
    @Id
    @Column(name="mfds1")
    private Integer mfds1;

    @Column(name="message")
    private String message;

    @Column(name="fault_code")
    private String faultCode;

    @Column(name="fualt_gubun")
    private Integer faultGubun;

    @Builder
    public FaultCode(Integer mfds1, String message, String faultCode, Integer faultGubun) {
        this.mfds1 = mfds1;
        this.message = message;
        this.faultCode = faultCode;
        this.faultGubun = faultGubun;
    }
}
