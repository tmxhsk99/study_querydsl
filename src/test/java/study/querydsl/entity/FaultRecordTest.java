package study.querydsl.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FaultRecordTest {
    @PersistenceContext
    EntityManager em;

    @Test
    public void testCompositeKeyObjectEntity(){

        FaultRecord faultRecord1 = new FaultRecord();
        faultRecord1.setFaultCode(1);
        faultRecord1.setDeviceCode(1);
        faultRecord1.setPvid("1");
        faultRecord1.setFaultGroup(1);
        faultRecord1.setSpeed(100);


        FaultRecord faultRecord2 = new FaultRecord();
        faultRecord2.setFaultCode(1);
        faultRecord2.setDeviceCode(2);
        faultRecord2.setPvid("100");
        faultRecord2.setFaultGroup(1);
        faultRecord2.setSpeed(2100);

        em.persist(faultRecord1);
        em.persist(faultRecord2);

        em.flush();
        em.close();


        FaultRecordId faultRecordId = new FaultRecordId("1", 1, 1);
        FaultRecord findfaultRecord = em.find(FaultRecord.class, faultRecordId);

        System.out.println("faultRecord = " + findfaultRecord.toString());

        FaultRecordId faultRecordId2 = new FaultRecordId("100", 1, 2);
        FaultRecord findfaultRecord2 = em.find(FaultRecord.class, faultRecordId2);

        System.out.println("faultRecord = " + findfaultRecord2.toString());


    }

}