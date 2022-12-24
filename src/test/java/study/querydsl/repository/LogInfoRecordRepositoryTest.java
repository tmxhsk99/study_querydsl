package study.querydsl.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.LogInfoRecord;
import study.querydsl.entity.LogInfoRecordId;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class LogInfoRecordRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    LogInfoRecordRepository logInfoRecordRepository;

    @Test
    public void basicTest() throws RuntimeException {
        LogInfoRecord logInfoRecord = LogInfoRecord.builder()
                .faultCode(1)
                .faultGroup(1)
                .oid("111")
                .deviceCode(1)
                .speed(200)
                .build();

        logInfoRecordRepository.save(logInfoRecord);

        LogInfoRecord findLogInfoRecord = logInfoRecordRepository.findById(LogInfoRecordId.builder()
                .faultCode(1)
                .oid("111")
                .deviceCode(1)
                .build()).orElseThrow(() -> new RuntimeException("Not Found FaultRecord!!!"));

        Assertions.assertThat(logInfoRecord).isEqualTo(findLogInfoRecord);

    }

}