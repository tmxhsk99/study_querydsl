package study.querydsl.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static study.querydsl.entity.QLogInfoRecord.*;

@SpringBootTest
@Transactional
public class QuerydslLogInfoRecordTest {

    @PersistenceContext
    EntityManager em;

    JPQLQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
        DeviceInfo[] deviceInfos = new DeviceInfo[3];
        FaultCode[] faultCodes = new FaultCode[3];

        for(int i = 0 ; i< 3 ; i++){
            //DeviceInfo Data
            deviceInfos[i] = DeviceInfo.builder()
                    .deviceCode(i)
                    .name("디바이스명 " + i)
                    .build();
            //FaultCode Data
            faultCodes[i] = FaultCode.builder()
                    .faultCode(String.valueOf(i))
                    .message("고장코드 메시지 "  + i )
                    .faultGubun(i)
                    .mfds1(i)
                    .build();

            em.persist(deviceInfos[i]);
            em.persist(faultCodes[i]);

        }
    }

    @Test
    public void noRelationJoinTest(){
        LogInfoRecord logInfoRecord = LogInfoRecord.builder()
                .oid("111")
                .faultCode(0)
                .deviceCode(0)
                .speed(100)
                .faultGroup(0)
                .build();

        LogInfoRecord logInfoRecord2 = LogInfoRecord.builder()
                .oid("222")
                .faultCode(1)
                .deviceCode(1)
                .speed(200)
                .faultGroup(1)
                .build();

        LogInfoRecord logInfoRecord3 = LogInfoRecord.builder()
                .oid("333")
                .faultCode(2)
                .deviceCode(2)
                .speed(300)
                .faultGroup(2)
                .build();

        em.persist(logInfoRecord);
        em.persist(logInfoRecord2);
        em.persist(logInfoRecord3);

        List<Tuple> result = queryFactory
            .select(QLogInfoRecord.logInfoRecord, QFaultCode.faultCode1, QDeviceInfo.deviceInfo.name)
                .from(QLogInfoRecord.logInfoRecord)
                .leftJoin(QDeviceInfo.deviceInfo).on(QLogInfoRecord.logInfoRecord.deviceCode.eq(QDeviceInfo.deviceInfo.deviceCode))
                .leftJoin(QFaultCode.faultCode1).on(QLogInfoRecord.logInfoRecord.faultCode.eq(QFaultCode.faultCode1.mfds1))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }
}
