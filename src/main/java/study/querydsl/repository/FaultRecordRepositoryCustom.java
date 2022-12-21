package study.querydsl.repository;

import com.querydsl.core.Tuple;
import study.querydsl.request.FaultRecordSearch;

import java.util.List;

public interface FaultRecordRepositoryCustom {
    public List<Tuple> searchFaultRecordList(FaultRecordSearch search);
}
