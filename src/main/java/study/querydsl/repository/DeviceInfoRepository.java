package study.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.querydsl.entity.DeviceInfo;

public interface DeviceInfoRepository extends JpaRepository<DeviceInfo,Integer> {
}
