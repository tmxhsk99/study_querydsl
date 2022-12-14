package study.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.querydsl.entity.FaultRecord;
import study.querydsl.entity.FaultRecordId;

public interface FaultRecordRepository extends JpaRepository<FaultRecord, FaultRecordId>,FaultRecordRepositoryCustom {

}
