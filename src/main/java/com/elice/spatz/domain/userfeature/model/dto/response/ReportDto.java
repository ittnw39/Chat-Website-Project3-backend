package com.elice.spatz.domain.userfeature.model.dto.response;

import com.elice.spatz.domain.userfeature.model.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportDto {
    private Long id;
    private Long reporterId;
    private Long reportedId;
    private String reportReason;
    private String reportUrl;
    private Status reportStatus;
}
