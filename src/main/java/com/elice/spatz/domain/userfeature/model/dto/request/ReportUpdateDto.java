package com.elice.spatz.domain.userfeature.model.dto.request;

import com.elice.spatz.domain.userfeature.model.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReportUpdateDto {
    private int id;
    private String reportReason;
    private String reportUrl;
    private Status reportStatus;
}
