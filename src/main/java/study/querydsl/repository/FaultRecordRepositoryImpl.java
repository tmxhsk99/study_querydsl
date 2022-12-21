package study.querydsl.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import study.querydsl.request.FaultRecordSearch;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static study.querydsl.entity.QDeviceInfo.*;
import static study.querydsl.entity.QFaultCode.*;
import static study.querydsl.entity.QFaultRecord.*;

public class FaultRecordRepositoryImpl implements FaultRecordRepositoryCustom{

    private final JPQLQueryFactory queryfactory;

    public FaultRecordRepositoryImpl(EntityManager em) {
        this.queryfactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Tuple> searchFaultRecordList(FaultRecordSearch search) {
        List<Tuple> result = queryfactory
                .select(faultRecord, faultCode1, deviceInfo)
                .from(faultRecord)
                .leftJoin(faultCode1).on(faultRecord.faultCode.eq(faultCode1.mfds1))
                .leftJoin(deviceInfo).on(faultRecord.deviceCode.eq(deviceInfo.deviceCode))
                .where(
                        pvidsEq(search.getPvids()),
                        localDateTimeFilter(search.getStartDate(), search.getEndDate())
                ).orderBy(faultRecord.occurDate.desc())
                .fetch();
        return result;
    }

    /**
     * 기간 검색을 위한 필터 메서드
     * @param startDate
     * @param endDate
     * @return
     */
    private BooleanExpression localDateTimeFilter(LocalDateTime startDate, LocalDateTime endDate){
        BooleanExpression isGoeStartDateTime = faultRecord.occurTime.goe(startDate);
        BooleanExpression isLoeEndDateTime = faultRecord.occurTime.loe(endDate);

        return Expressions.allOf(isGoeStartDateTime,isLoeEndDateTime);
    }

    private BooleanExpression pvidsEq(List<String> pvids){
        return pvids != null ? faultRecord.pvid.in(pvids) : null;
    }

}
