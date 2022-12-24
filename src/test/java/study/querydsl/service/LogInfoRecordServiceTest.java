package study.querydsl.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.DeviceInfo;
import study.querydsl.entity.FaultCode;
import study.querydsl.entity.LogInfoRecord;
import study.querydsl.repository.DeviceInfoRepository;
import study.querydsl.repository.FaultCodeRepository;
import study.querydsl.repository.LogInfoRecordRepository;
import study.querydsl.request.LogInfoRecordSearch;
import study.querydsl.response.FaultRecordResponse;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class LogInfoRecordServiceTest {
    @Autowired
    LogInfoRecordRepository logInfoRecordRepository;

    @Autowired
    FaultCodeRepository faultCodeRepository;

    @Autowired
    DeviceInfoRepository deviceInfoRepository;

    @Autowired
    FaultRecordService faultRecordService;

    @Autowired
    EntityManager em;

    /**
     * 로컬 테스트 DB 시 사용
     */
    //@BeforeEach
    @DisplayName("테이블 초기화")
    public void setting() {
        deviceInfoRepository.deleteAll();
        logInfoRecordRepository.deleteAll();
        faultCodeRepository.deleteAll();
    }

    @Test
    @DisplayName(" 기간 조회 성공 테스트 ")
    public void getList_localDB_ShouldPass() throws InterruptedException {
        //given
        DeviceInfo[] deviceInfos = new DeviceInfo[3];
        FaultCode[] faultCodes = new FaultCode[3];
        LogInfoRecord[] logInfoRecords = new LogInfoRecord[3];
        for(int i = 0;i < 3;i++){
            // DeviceInfo Data
            deviceInfos[i] = DeviceInfo.builder()
                    .deviceCode(i)
                    .name("디바이스명 " + i)
                    .build();
            // FaultCode Data
            faultCodes[i] = FaultCode.builder()
                    .faultCode(String.valueOf(i))
                    .message("고장코드 메시지 "  + i )
                    .faultGubun(i)
                    .mfds1(i)
                    .build();
            // FaultRecord Data
            logInfoRecords[i] = LogInfoRecord.builder()
                    .oid("oid" + i)
                    .occurDate(DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now()))
                    .occurTime(LocalDateTime.now())
                    .deviceCode(i)
                    .faultCode(i)
                    .speed(i+100)
                    .faultGroup(i)
                    .build();
            deviceInfoRepository.save(deviceInfos[i]);
            faultCodeRepository.save(faultCodes[i]);
            logInfoRecordRepository.save(logInfoRecords[i]);
            em.flush();
            em.clear();
            //insert 순서 차이를 주기위해 sleep 초단위로 정렬을하기때문에 1초를 준다.
            Thread.currentThread().sleep(1000);
        }
        //when
        // 기간 검색시 startDateTime , endDateTime이 존재해야한다
        int sYear = 2022;
        int sMonth = 12;
        int sDay = 20;
        int sHour = 12;
        int sMin = 20;
        int sSec = 15;

        int eYear = 2022;
        int eMonth = 12;
        int eDay = 22;
        int eHour = 12;
        int eMin = 20;
        int eSec = 15;

        LogInfoRecordSearch search = LogInfoRecordSearch.builder()
                .startDate(LocalDateTime.of(sYear,sMonth,sDay,sHour,sMin,sSec))
                .endDate(LocalDateTime.of(eYear,eMonth,eDay,eHour,eMin,eSec))
                .build();

        List<FaultRecordResponse> list = faultRecordService.getList(search);
        // than
        // 최신순으로 정렬되므로 역순으로 리스트가 나온다...
        log.info("Search Fault Record List : " + list.toString());
    }

}