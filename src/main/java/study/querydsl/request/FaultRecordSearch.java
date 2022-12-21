package study.querydsl.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaultRecordSearch {
    private String pvid;

    private List<String> pvids = new ArrayList<>();

    private String occurDate;

    private Integer speed;

    private Integer faultGroup;

    private LocalDateTime occurTime;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
