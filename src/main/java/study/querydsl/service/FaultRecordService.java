package study.querydsl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.querydsl.repository.LogInfoRecordRepository;
import study.querydsl.request.LogInfoRecordSearch;
import study.querydsl.response.FaultRecordResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaultRecordService {
    private final LogInfoRecordRepository logInfoRecordRepository;

    public List<FaultRecordResponse> getList(LogInfoRecordSearch search){
        return logInfoRecordRepository
                .searchFaultRecordList(search)
                .stream().map(FaultRecordResponse::new)
                .collect(Collectors.toList());
    }
}
