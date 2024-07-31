package com.elice.spatz.domain.userfeature.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlockDto {
    private long blockerId;
    private long blockedId;
    private boolean isBlocked;
}
