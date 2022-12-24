package study.querydsl.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import study.querydsl.entity.LogInfoRecord;
import study.querydsl.entity.QDeviceInfo;
import study.querydsl.entity.QFaultCode;
import study.querydsl.entity.QLogInfoRecord;
import study.querydsl.request.LogInfoRecordSearch;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static study.querydsl.entity.QDeviceInfo.deviceInfo;
import static study.querydsl.entity.QFaultCode.faultCode1;
import static study.querydsl.entity.QLogInfoRecord.logInfoRecord;


public class LogInfoRecordRepositoryImpl implements LogInfoRecordRepositoryCustom {

    private final JPQLQueryFactory queryfactory;

    public LogInfoRecordRepositoryImpl(EntityManager em) {
        this.queryfactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Tuple> searchFaultRecordList(LogInfoRecordSearch search) {
        return  queryfactory
                .select(logInfoRecord, faultCode1, deviceInfo)
                .from(logInfoRecord)
                .leftJoin(faultCode1).on(logInfoRecord.faultCode.eq(faultCode1.mfds1))
                .leftJoin(deviceInfo).on(logInfoRecord.deviceCode.eq(deviceInfo.deviceCode))
                .where(
                        oidsEq(search.getOids()),
                        oidEq(search.getOid()),
                        localDateTimeFilter(search.getStartDate(), search.getEndDate())
                ).fetch();
    }


    /**
     * 기간 검색을 위한 필터 메서드
     * @param startDate
     * @param endDate
     * @return
     */

    private BooleanExpression localDateTimeFilter(LocalDateTime startDate, LocalDateTime endDate){
        BooleanExpression isGoeStartDateTime = startDate != null ? logInfoRecord.occurTime.goe(startDate) : null;
        BooleanExpression isLoeEndDateTime = endDate != null ? logInfoRecord.occurTime.loe(endDate) : null;
        return Expressions.allOf(isGoeStartDateTime,isLoeEndDateTime);
    }

    private BooleanExpression oidsEq(List<String> oids){
        return oids != null ? logInfoRecord.oid.in(oids) : null;
    }

    private BooleanExpression oidEq(String oid) {
        return oid != null ? logInfoRecord.oid.eq(oid) : null;
    }

}
