package study.querydsl.repository;

import com.querydsl.core.Tuple;
import study.querydsl.request.LogInfoRecordSearch;

import java.util.List;

public interface LogInfoRecordRepositoryCustom {
    public List<Tuple> searchFaultRecordList(LogInfoRecordSearch search);
}
