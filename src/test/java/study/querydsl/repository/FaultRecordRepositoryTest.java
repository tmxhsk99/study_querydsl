package study.querydsl.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.FaultRecord;
import study.querydsl.entity.FaultRecordId;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FaultRecordRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    FaultRecordRepository faultRecordRepository;

    @Test
    public void basicTest() throws RuntimeException {
        FaultRecord faultRecord = FaultRecord.builder()
                .faultCode(1)
                .faultGroup(1)
                .pvid("111")
                .deviceCode(1)
                .speed(200)
                .build();

        faultRecordRepository.save(faultRecord);

        FaultRecord findFaultRecord = faultRecordRepository.findById(FaultRecordId.builder()
                .faultCode(1)
                .pvid("111")
                .deviceCode(1)
                .build()).orElseThrow(() -> new RuntimeException("Not Found FaultRecord!!!"));

        Assertions.assertThat(faultRecord).isEqualTo(findFaultRecord);

    }

}