package study.querydsl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.querydsl.repository.FaultRecordRepository;
import study.querydsl.request.FaultRecordSearch;
import study.querydsl.response.FaultRecordResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaultRecordService {
    private final FaultRecordRepository faultRecordRepository;

    public List<FaultRecordResponse> getList(FaultRecordSearch search){
        return faultRecordRepository
                .searchFaultRecordList(search)
                .stream().map(FaultRecordResponse::new)
                .collect(Collectors.toList());
    }
}
