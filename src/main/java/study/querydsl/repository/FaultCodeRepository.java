package study.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.querydsl.entity.FaultCode;

public interface FaultCodeRepository extends JpaRepository <FaultCode,Integer> {
}
