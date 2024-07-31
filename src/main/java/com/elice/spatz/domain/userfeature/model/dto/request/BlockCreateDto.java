package com.elice.spatz.domain.userfeature.model.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockCreateDto {
    private long blockerId;
    private long blockedId;
    private boolean isBlocked;
}
