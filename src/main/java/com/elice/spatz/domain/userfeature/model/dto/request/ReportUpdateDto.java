package com.elice.spatz.domain.userfeature.model.dto.request;

import com.elice.spatz.domain.userfeature.model.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReportUpdateDto {
    private long id;
    private long reporterId;
    private long reportedId;
    private String reportReason;
}
