package study.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.querydsl.entity.LogInfoRecord;
import study.querydsl.entity.LogInfoRecordId;

public interface LogInfoRecordRepository extends JpaRepository<LogInfoRecord, LogInfoRecordId>, LogInfoRecordRepositoryCustom {

}
