package study.querydsl.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
class LogInfoRecordTest {
    @PersistenceContext
    EntityManager em;

    @Test
    public void testCompositeKeyObjectEntity(){

        LogInfoRecord logInfoRecord1 = new LogInfoRecord();
        logInfoRecord1.setFaultCode(1);
        logInfoRecord1.setDeviceCode(1);
        logInfoRecord1.setOid("1");
        logInfoRecord1.setFaultGroup(1);
        logInfoRecord1.setSpeed(100);


        LogInfoRecord logInfoRecord2 = new LogInfoRecord();
        logInfoRecord2.setFaultCode(1);
        logInfoRecord2.setDeviceCode(2);
        logInfoRecord2.setOid("100");
        logInfoRecord2.setFaultGroup(1);
        logInfoRecord2.setSpeed(2100);

        em.persist(logInfoRecord1);
        em.persist(logInfoRecord2);

        em.flush();
        em.close();


        LogInfoRecordId faultRecordId = new LogInfoRecordId("1", 1, 1);
        LogInfoRecord findfaultRecord = em.find(LogInfoRecord.class, faultRecordId);

        System.out.println("faultRecord = " + findfaultRecord.toString());

        LogInfoRecordId faultRecordId2 = new LogInfoRecordId("100", 1, 2);
        LogInfoRecord findfaultRecord2 = em.find(LogInfoRecord.class, faultRecordId2);

        System.out.println("faultRecord = " + findfaultRecord2.toString());

    }

}